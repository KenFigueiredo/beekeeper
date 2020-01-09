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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.thrift.TException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HiveClientTest {

  private static final String DATABASE = "some_db";
  private static final String DROP_TABLE_NAME = "drop_table";

  @Mock private static HiveMetaStoreClient metaStoreClient;

  @Test
  public void typicalLiveDropTable() throws TException {
    HiveClient hiveClient = new HiveClient(metaStoreClient, false);
    boolean result = hiveClient.dropTable(DATABASE, DROP_TABLE_NAME);
    verify(metaStoreClient).dropTable(DATABASE, DROP_TABLE_NAME);
    assertThat(result).isTrue();
  }

  @Test
  public void failedLiveDropTable() throws TException {
    HiveClient hiveClient = new HiveClient(metaStoreClient, false);
    doThrow(new TException("error")).when(metaStoreClient).dropTable(DATABASE, DROP_TABLE_NAME);
    boolean result = hiveClient.dropTable(DATABASE, DROP_TABLE_NAME);
    assertThat(result).isFalse();
  }

  @Test
  public void typicalDryRunDropTable() throws TException {
    HiveClient hiveClient = new HiveClient(metaStoreClient, true);
    boolean result = hiveClient.dropTable(DATABASE, DROP_TABLE_NAME);
    verify(metaStoreClient, never()).dropTable(DATABASE, DROP_TABLE_NAME);
    assertThat(result).isTrue();
  }
}
