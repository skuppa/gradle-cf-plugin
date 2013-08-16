/*
 * Copyright 2012 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package org.gradle.cf

import org.gradle.api.tasks.TaskAction
import org.cloudfoundry.client.lib.domain.CloudInfo
import org.cloudfoundry.client.lib.domain.CloudServiceOffering

/**
 * A basic task which can be used to get info from CloudFoundry platform.
 */
class InfoCloudFoundryTask extends AbstractCloudFoundryTask {

    InfoCloudFoundryTask() {
        super()
        description = 'Displays information about the target CloudFoundry platform'
    }

    @TaskAction
    void info() {
        connectToCloudFoundry()
        CloudInfo info = client?.cloudInfo
        List<CloudServiceOffering> configurations = client?.serviceOfferings
        log """Platform name: $info.name
Platform version: $info.version build $info.build
Debug ${info.allowDebug?'on':'off'}
Support: $info.support
Platform description: $info.description


Service configurations:
   ${configurations.collect { CloudServiceOffering it ->
            "   Vendor: ${it.vendor.padRight(16)} Type: ${it.type.padRight(16)} Version: ${it.version.padRight(6)} Desc.: $it.description"
        }.join('\n   ')}

Usage:
    Applications        : $info.usage.apps/$info.limits.maxApps
    Services            : $info.usage.services/$info.limits.maxServices
    Memory              : $info.usage.totalMemory MB/$info.limits.maxTotalMemory MB
    URIs per application: $info.usage.urisPerApp/$info.limits.maxUrisPerApp
"""
    }
}
