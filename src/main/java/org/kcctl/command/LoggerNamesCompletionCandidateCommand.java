/*
 *  Copyright 2021 The original authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.kcctl.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.kcctl.service.KafkaConnectApi;
import org.kcctl.util.ConfigurationContext;

import com.fasterxml.jackson.databind.node.ObjectNode;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;

import static picocli.CommandLine.*;

@Command(name = "logger-name-completions", hidden = true)
public class LoggerNamesCompletionCandidateCommand implements Runnable {

    @Spec
    private CommandSpec spec;

    private final ConfigurationContext context;

    @Inject
    public LoggerNamesCompletionCandidateCommand(ConfigurationContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        KafkaConnectApi kafkaConnectApi = RestClientBuilder.newBuilder()
                .baseUri(context.getCurrentContext().getCluster())
                .build(KafkaConnectApi.class);

        ObjectNode connectorLoggers = kafkaConnectApi.getLoggers("");
        Iterator<String> fieldNames = connectorLoggers.fieldNames();
        List<String> loggers = new ArrayList<>();
        fieldNames.forEachRemaining(loggers::add);

        spec.commandLine().getOut().println(String.join(" ", loggers));
    }
}
