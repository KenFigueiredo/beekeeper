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
package com.expediagroup.beekeeper.core.monitoring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.expediagroup.beekeeper.core.model.HousekeepingPath;

/**
 * Times the annotated method by triggering {@link TimedHousekeepingPathAspect}.
 *
 * The method can have any number of arguments but a {@link HousekeepingPath} must be the first. Will add the fully
 * qualified table name to the timer metric as a tag.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimedHousekeepingPath {

  /**
   * Name of the TimedHousekeepingPath metric.
   */
  String value();

}
