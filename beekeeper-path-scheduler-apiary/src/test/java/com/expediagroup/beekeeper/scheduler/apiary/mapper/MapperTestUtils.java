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
package com.expediagroup.beekeeper.scheduler.apiary.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import com.expedia.apiary.extensions.receiver.common.event.ListenerEvent;
import com.expedia.apiary.extensions.receiver.common.messaging.MessageEvent;
import com.expedia.apiary.extensions.receiver.common.messaging.MessageProperty;
import com.expedia.apiary.extensions.receiver.sqs.messaging.SqsMessageProperty;

import com.expediagroup.beekeeper.core.model.HousekeepingPath;
import com.expediagroup.beekeeper.core.model.LifecycleEventType;

public class MapperTestUtils {

  private static final String RECEIPT_HANDLE = "receiptHandle";

  public static MessageEvent newMessageEvent(ListenerEvent event) {
    return new MessageEvent(event, MapperTestUtils.newMessageProperties());
  }

  private static Map<MessageProperty, String> newMessageProperties() {
    return Collections.singletonMap(SqsMessageProperty.SQS_MESSAGE_RECEIPT_HANDLE, RECEIPT_HANDLE);
  }

  static void assertPath(
      HousekeepingPath path,
      String cleanupDelay,
      String tableName,
      String dbName,
      Integer cleanupAttempts,
      LocalDateTime creationTimestamp,
      String pathToCleanup,
      LifecycleEventType lifeCycleEventType
  ) {
    LocalDateTime now = LocalDateTime.now();
    assertThat(path.getPath()).isEqualTo(pathToCleanup);
    assertThat(path.getTableName()).isEqualTo(tableName);
    assertThat(path.getDatabaseName()).isEqualTo(dbName);
    assertThat(path.getCleanupAttempts()).isEqualTo(cleanupAttempts);
    assertThat(path.getCleanupDelay()).isEqualTo(Duration.parse(cleanupDelay));
    assertThat(path.getLifecycleType()).isEqualToIgnoringCase(lifeCycleEventType.toString());
    assertThat(path.getModifiedTimestamp()).isNull();
    assertThat(path.getCreationTimestamp()).isBetween(creationTimestamp, now);
    assertThat(path.getCleanupTimestamp()).isEqualTo(path.getCreationTimestamp().plus(Duration.parse(cleanupDelay)));
  }
}