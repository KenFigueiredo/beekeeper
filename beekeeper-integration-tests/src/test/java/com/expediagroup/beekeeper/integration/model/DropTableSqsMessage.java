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
package com.expediagroup.beekeeper.integration.model;

import java.io.IOException;
import java.net.URL;

import com.expedia.apiary.extensions.receiver.common.event.EventType;

public class DropTableSqsMessage extends SqsMessageFile {

  private static final URL DROP_TABLE_FILE = SqsMessageFile.class.getResource("/drop_table.json");

  DropTableSqsMessage() throws IOException {
    setMessageFromFile(DROP_TABLE_FILE);
  }

  public DropTableSqsMessage(
      String tableLocation,
      Boolean isUnreferenced,
      Boolean isExpired,
      Boolean isWhitelisted
  ) throws IOException {
    setMessageFromFile(DROP_TABLE_FILE);
    setTableLocation(tableLocation);
    setUnreferenced(isUnreferenced);
    setExpired(isExpired);
    setWhitelisted(EventType.DROP_TABLE, isWhitelisted);
  }

  @Override
  public String getFormattedString() {
    return String.format(message, tableLocation, isUnreferenced, isExpired, isWhitelisted);
  }
}