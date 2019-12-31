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

import com.expedia.apiary.extensions.receiver.common.event.EventType;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AlterPartitionSqsMessage extends SqsMessageFile {
    private static URL ALTER_PARTITION_FILE = SqsMessageFile.class.getResource("/alter_partition.json");

    public AlterPartitionSqsMessage() throws IOException {
        this.setMessageFromFile(ALTER_PARTITION_FILE);
    }

    public AlterPartitionSqsMessage(
            String tableLocation,
            String partitionLocation,
            String oldPartitionLocation,
            Boolean isUnreferenced,
            Boolean isExpired,
            Boolean isWhitelisted
    ) throws IOException {
        this.setMessageFromFile(ALTER_PARTITION_FILE);
        this.setTableLocation(tableLocation);
        this.setPartitionLocation(partitionLocation);
        this.setOldPartitionLocation(oldPartitionLocation);
        this.setUnreferenced(isUnreferenced);
        this.setExpired(isExpired);
        this.setWhitelisted(EventType.ALTER_PARTITION, isWhitelisted);
    }

    public String getFormattedString() {
        return String.format(message,
                tableLocation,
                partitionLocation,
                oldPartitionLocation,
                isUnreferenced, isExpired, isWhitelisted);
    }

}
