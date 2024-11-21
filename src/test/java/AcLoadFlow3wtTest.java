/**
 * Copyright (c) 2019, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * SPDX-License-Identifier: MPL-2.0
 */

import com.powsybl.iidm.network.Battery;
import com.powsybl.iidm.network.Bus;
import com.powsybl.iidm.network.Generator;
import com.powsybl.iidm.network.Network;
import com.powsybl.iidm.network.extensions.VoltageRegulationAdder;
import com.powsybl.iidm.network.test.BatteryNetworkFactory;
import com.powsybl.loadflow.LoadFlow;
import com.powsybl.loadflow.LoadFlowParameters;
import com.powsybl.loadflow.LoadFlowResult;
import com.powsybl.math.matrix.DenseMatrixFactory;
import com.powsybl.openloadflow.OpenLoadFlowProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AcLoadFlow3wtTest {

    private Network network;
    private Bus genBus;
    private Bus batBus;
    private Generator generator;
    private Battery battery1;
    private Battery battery2;
    private LoadFlow.Runner loadFlowRunner;
    private LoadFlowParameters parameters;

    @BeforeEach
    void setUp() {
        network = BatteryNetworkFactory.create();
        genBus = network.getBusBreakerView().getBus("NGEN");
        batBus = network.getBusBreakerView().getBus("NBAT");
        generator = network.getGenerator("GEN");
        generator.setMinP(0).setMaxP(1000).setTargetV(401.);
        battery1 = network.getBattery("BAT");
        battery1.setMinP(-1000).setMaxP(1000).setTargetQ(0).setTargetP(0);
        battery2 = network.getBattery("BAT2");
        battery2.setTargetP(-1000).setMaxP(1000);

        loadFlowRunner = new LoadFlow.Runner(new OpenLoadFlowProvider(new DenseMatrixFactory()));
        parameters = new LoadFlowParameters().setUseReactiveLimits(true)
                .setDistributedSlack(true);
    }

    @Test
    void test() {
        battery2.newExtension(VoltageRegulationAdder.class)
                .withTargetV(401)
                .withVoltageRegulatorOn(false)

                .add();
        LoadFlowResult result = loadFlowRunner.run(network, parameters);
//        assertTrue(result.isFullyConverged());
//
//        assertVoltageEquals(401, genBus);
//        LoadFlowAssert.assertAngleEquals(5.916585, genBus);
//        assertVoltageEquals(397.660, batBus);
//        LoadFlowAssert.assertAngleEquals(0.0, batBus);
    }
}
