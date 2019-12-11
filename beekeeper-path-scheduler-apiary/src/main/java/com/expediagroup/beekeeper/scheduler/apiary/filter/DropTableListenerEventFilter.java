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
package com.expediagroup.beekeeper.scheduler.apiary.filter;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.expedia.apiary.extensions.receiver.common.event.DropTableEvent;
import com.expedia.apiary.extensions.receiver.common.event.ListenerEvent;

@Component
public class DropTableListenerEventFilter implements ListenerEventFilter {

  private static final String DROP_TABLE_WHITELIST_PARAMETER = "beekeeper.permit.drop.table";

  @Override
  public boolean filter(ListenerEvent listenerEvent) {
    if (listenerEvent == null) {
      return true;
    }
    Class<? extends ListenerEvent> eventClass = listenerEvent.getEventType().eventClass();
    if (!DropTableEvent.class.equals(eventClass)) {
      return false;
    }
    Map<String, String> tableParameters = listenerEvent.getTableParameters();
    if (tableParameters == null) {
      return true;
    }
    return !Boolean.valueOf(tableParameters.get(DROP_TABLE_WHITELIST_PARAMETER));
  }
}