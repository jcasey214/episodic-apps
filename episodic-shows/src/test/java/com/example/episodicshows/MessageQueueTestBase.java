package com.example.episodicshows;

import org.junit.ClassRule;
import org.springframework.amqp.rabbit.junit.BrokerRunning;

public class MessageQueueTestBase {
    @ClassRule
    public static BrokerRunning brokerRunning = BrokerRunning.isRunningWithEmptyQueues("episodic-progress");

//    @AfterClass
//    public static void tearDown() {
//        brokerRunning.removeTestQueues();
//    }
}
