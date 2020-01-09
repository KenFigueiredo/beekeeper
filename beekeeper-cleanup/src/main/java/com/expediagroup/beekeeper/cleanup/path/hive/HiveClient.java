/**
 * Copyright (C) 2019-2020 Expedia, Inc.
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
package com.expediagroup.beekeeper.cleanup.path.hive;

import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HiveClient {

  private static final Logger log = LoggerFactory.getLogger(HiveClient.class);
  @Autowired private final HiveMetaStoreClient metaStoreClient;
  private final boolean dryRunEnabled;

  public HiveClient(
      HiveMetaStoreClient metaStoreClient,
      @Value("${properties.dry-run-enabled}") boolean dryRunEnabled
  ) {
    this.metaStoreClient = metaStoreClient;
    this.dryRunEnabled = dryRunEnabled;
  }

  boolean dropTable(String databaseName, String tableName) {
    if (dryRunEnabled) {
      log.info("[DRY RUN] Would have dropped Table:{} from Database:{}.", tableName, databaseName);
      return true;
    }

    try {
      metaStoreClient.dropTable(databaseName, tableName);
      log.info("Successfully dropped Table:{} from Database:{}", tableName, databaseName);
    } catch (TException e) {
      log.error("Failed to drop Table:{} from Database:{}", tableName, databaseName, e);
      return false;
    }

    return true;
  }
}
