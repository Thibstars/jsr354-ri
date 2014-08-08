/**
 * Copyright (c) 2012, 2014, Credit Suisse (Anatole Tresch), Werner Keil and others by the @author tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.javamoney.moneta.function;

import org.javamoney.moneta.QueryTypes;

import javax.money.*;
import javax.money.spi.RoundingProviderSpi;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class TestRoundingProvider implements RoundingProviderSpi{

    private Set<String> customIds = new HashSet<>();

    public TestRoundingProvider(){
        customIds.add("zero");
        customIds.add("minusOne");
        customIds.add("CHF-cash");
    }

    private MonetaryRounding zeroRounding = new MonetaryRounding(){
        private final RoundingContext CONTEXT = RoundingContextBuilder.create("TestRoundingProvider", "zero").build();

        @Override
        public RoundingContext getRoundingContext(){
            return CONTEXT;
        }

        @Override
        public MonetaryAmount apply(MonetaryAmount amount){
            return amount.getFactory().setCurrency(amount.getCurrency()).setNumber(0L).create();
        }

    };

    private MonetaryRounding minusOneRounding = new MonetaryRounding(){
        private final RoundingContext CONTEXT =
                RoundingContextBuilder.create("TestRoundingProvider", "minusOne").build();

        @Override
        public RoundingContext getRoundingContext(){
            return CONTEXT;
        }

        @Override
        public MonetaryAmount apply(MonetaryAmount amount){
            return amount.getFactory().setCurrency(amount.getCurrency()).setNumber(-1).create();
        }
    };

    private MonetaryRounding chfCashRounding = new MonetaryRounding(){
        private final RoundingContext CONTEXT =
                RoundingContextBuilder.create("TestRoundingProvider", "chfCashRounding").build();

        @Override
        public RoundingContext getRoundingContext(){
            return CONTEXT;
        }

        @Override
        public MonetaryAmount apply(MonetaryAmount amount){
            MonetaryOperator minorRounding = MonetaryRoundings
                    .getRounding(RoundingQueryBuilder.create().set("scale", 2).set(RoundingMode.HALF_UP).build());
            MonetaryAmount amt = amount.with(minorRounding);
            MonetaryAmount mp = amt.with(MonetaryFunctions.minorPart());
            if(mp.isGreaterThanOrEqualTo(
                    MonetaryAmounts.getDefaultAmountFactory().setCurrency(amount.getCurrency()).setNumber(0.03)
                            .create())){
                // add
                return amt.add(MonetaryAmounts.getDefaultAmountFactory().setCurrency(amt.getCurrency())
                                       .setNumber(new BigDecimal("0.05")).create().subtract(mp));
            }else{
                // subtract
                return amt.subtract(mp);
            }
        }
    };

    @Override
    public MonetaryRounding getRounding(RoundingQuery roundingQuery){
        Long timestamp = roundingQuery.getTimestampMillis();
        if(roundingQuery.getRoundingName() == null){
            return getRounding(roundingQuery, timestamp, "default");
        }else{
            return getRounding(roundingQuery, timestamp, roundingQuery.getRoundingName());
        }
    }

    @Override
    public Set<QueryType> getQueryTypes() {
        return QueryType.DEFAULT_SET;
    }

    private MonetaryRounding getRounding(RoundingQuery roundingQuery, Long timestamp, String roundingId){
        if("foo".equals(roundingId)){
            return null;
        }
        if("default".equals(roundingId)){
            CurrencyUnit currency = roundingQuery.getCurrencyUnit();
            if(Objects.nonNull(currency)){
                if(currency.getCurrencyCode().equals("XXX")){
                    if(timestamp > System.currentTimeMillis()){
                        return minusOneRounding;
                    }else{
                        return zeroRounding;
                    }
                }else if(roundingQuery.getBoolean("cashRounding", false)){
                    if(currency.getCurrencyCode().equals("CHF")){
                        return chfCashRounding;
                    }
                }
            }
        }else{
            MonetaryRounding r = getCustomRounding(roundingId);
            if(r != null){
                return r;
            }
        }
        return null;
    }


    private MonetaryRounding getCustomRounding(String customRoundingId){
        switch(customRoundingId){
            case "CHF-cash":
                return chfCashRounding;
            case "zero":
                return zeroRounding;
            case "minusOne":
                return minusOneRounding;
        }
        return null;
    }

    @Override
    public Set<String> getRoundingIds(){
        return customIds;
    }

}
