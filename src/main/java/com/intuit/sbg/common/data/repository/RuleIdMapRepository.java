/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.repository;

import com.intuit.sbg.common.data.domain.RuleIdMap;

/**
 * Created by vkumar21 on 4/6/17.
 */
public interface RuleIdMapRepository extends Repository<RuleIdMap> {
    /**
     *
     * @param ruleIdMap RuleIdMap with App1Mapper setting
     * @return RuleIdMap if exists otherwise null
     */
    RuleIdMap findByFirstApp(RuleIdMap ruleIdMap);

    /**
     *
     * @param ruleIdMap uleIdMap with App2Mapper setting
     * @return RuleIdMap if exists otherwise null
     */
    RuleIdMap findBySecondApp(RuleIdMap ruleIdMap);
}
