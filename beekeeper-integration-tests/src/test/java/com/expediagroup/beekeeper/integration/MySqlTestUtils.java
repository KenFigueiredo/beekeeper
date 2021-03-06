/**
 * Copyright (C) 2019 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.expediagroup.beekeeper.integration;

import static java.lang.String.format;
import static java.time.ZoneOffset.UTC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.expediagroup.beekeeper.core.model.EntityHousekeepingPath;
import com.expediagroup.beekeeper.core.model.PathStatus;

public class MySqlTestUtils {

  private static final String DROP_TABLE = "DROP TABLE IF EXISTS beekeeper.%s;";
  private static final String SELECT_TABLE = "SELECT * FROM beekeeper.%s;";
  private static final String INSERT_PATH = "INSERT INTO beekeeper.path (%s) VALUES (%s);";

  private static final String CLEANUP_ATTEMPTS = "cleanup_attempts";
  private static final String CLEANUP_DELAY = "cleanup_delay";
  private static final String CLEANUP_TIMESTAMP = "cleanup_timestamp";
  private static final String CLIENT_ID = "client_id";
  private static final String CREATION_TIMESTAMP = "creation_timestamp";
  private static final String DATABASE_NAME = "database_name";
  private static final String ID = "id";
  private static final String MODIFIED_TIMESTAMP = "modified_timestamp";
  private static final String PATH = "path";
  private static final String PATH_STATUS = "path_status";
  private static final String TABLE_NAME = "table_name";

  private Connection connection;

  public MySqlTestUtils(String jdbcUrl, String username, String password) throws SQLException {
    connection = DriverManager.getConnection(jdbcUrl, username, password);
  }

  public void insertPath(String path, String table) throws SQLException {
    String fields = String.join(", ", PATH, PATH_STATUS, CLEANUP_DELAY, CLEANUP_TIMESTAMP, TABLE_NAME);
    String values = Stream.of(path, PathStatus.SCHEDULED.toString(), "PT1S", Timestamp.valueOf(LocalDateTime.now(UTC)
        .minus(1L, ChronoUnit.DAYS))
        .toString(), table)
        .map(s -> "\"" + s + "\"")
        .collect(Collectors.joining(", "));

    connection.createStatement()
        .executeUpdate(format(INSERT_PATH, fields, values));
  }

  public int rowsInTable(String table) throws SQLException {
    ResultSet resultSet = connection.createStatement()
        .executeQuery(format(SELECT_TABLE, table));
    resultSet.last();
    return resultSet.getRow();
  }

  public void dropTable(String tableName) throws SQLException {
    connection.createStatement()
        .executeUpdate(format(DROP_TABLE, tableName));
  }

  public void close() throws SQLException {
    connection.close();
  }

  public List<EntityHousekeepingPath> getPaths() throws SQLException {
    ResultSet resultSet = connection.createStatement()
        .executeQuery(format(SELECT_TABLE, PATH));
    List<EntityHousekeepingPath> paths = new ArrayList<>();
    while (resultSet.next()) {
      paths.add(map(resultSet));
    }
    return paths;
  }

  private EntityHousekeepingPath map(ResultSet resultSet) throws SQLException {
    EntityHousekeepingPath path = new EntityHousekeepingPath.Builder()
        .id(resultSet.getLong(ID))
        .path(resultSet.getString(PATH))
        .pathStatus(PathStatus.valueOf(resultSet.getString(PATH_STATUS)))
        .cleanupAttempts(resultSet.getInt(CLEANUP_ATTEMPTS))
        .tableName(resultSet.getString(TABLE_NAME))
        .databaseName(resultSet.getString(DATABASE_NAME))
        .creationTimestamp(Timestamp.valueOf(resultSet.getString(CREATION_TIMESTAMP))
            .toLocalDateTime())
        .modifiedTimestamp(Timestamp.valueOf(resultSet.getString(MODIFIED_TIMESTAMP))
            .toLocalDateTime())
        .cleanupDelay(Duration.parse(resultSet.getString(CLEANUP_DELAY)))
        .clientId(resultSet.getString(CLIENT_ID))
        .build();
    path.setCleanupTimestamp(Timestamp.valueOf(resultSet.getString(CLEANUP_TIMESTAMP))
        .toLocalDateTime());
    return path;
  }
}
