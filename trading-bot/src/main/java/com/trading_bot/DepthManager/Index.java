package com.trading_bot.DepthManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class Index {
    private static final Logger logger = LoggerFactory.getLogger(Index.class); // Logger instance

    private DepthManager solInrMarket;
    private DepthManager usdtInrMarket;
    private DepthManager solUsdtMarket;

    public Index() {
        // Initialize DepthManager for different markets
        solInrMarket = new DepthManager("B-XAI_INR");
        usdtInrMarket = new DepthManager("B-USDT_INR");
        solUsdtMarket = new DepthManager("B-XAI_USDT");

        // Set up a timer to run every 3 seconds (3000ms)
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                logger.info("SOL-INR Market Depth - Highest Bid: {}, Lowest Ask: {}",
                        solInrMarket.getRelevantDepth1(), solInrMarket.getRelevantDepth2());
                logger.info("USDT-INR Market Depth - Highest Bid: {}, Lowest Ask: {}",
                        usdtInrMarket.getRelevantDepth1(), usdtInrMarket.getRelevantDepth2());
                logger.info("SOL-USDT Market Depth - Highest Bid: {}, Lowest Ask: {}",
                        solUsdtMarket.getRelevantDepth1(), solUsdtMarket.getRelevantDepth2());

                //SELL SOL FOR INR, BUY USDT FROM INR, BUY SOL FROM INR
                float canGetInr = solInrMarket.getRelevantDepth2() - (float)(0.001);
                float canGetUsdt = canGetInr / usdtInrMarket.getRelevantDepth2();
                float canGetSol = canGetUsdt / solUsdtMarket.getRelevantDepth2();
                logger.info("You can convert 1 sol to " + canGetSol + " sol");

                //BUY SOL FROM INR, SELL SOL FOR USDT, SELL USDT FOR INR
                float initialInr = solInrMarket.getRelevantDepth1() + (float) 0.001;
                float canGetUsdt2 = solUsdtMarket.getRelevantDepth1();
                float canGetInr2 = usdtInrMarket.getRelevantDepth1()*canGetUsdt2;

                logger.info("You can convert " + initialInr + " INR to " + canGetInr2);
            }
        }, 0, 3000); // 0 initial delay, repeat every 3000ms (3 seconds)
    }
}
