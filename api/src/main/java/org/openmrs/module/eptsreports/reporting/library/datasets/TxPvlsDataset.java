/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.library.datasets;

import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.BreastfeedingIndicators;
import org.openmrs.module.eptsreports.reporting.library.indicators.HivIndicators;
import org.openmrs.module.eptsreports.reporting.library.indicators.PregnantIndicators;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TxPvlsDataset extends BaseDataSet {
	
	@Autowired
	private HivIndicators hivIndicators;
	
	@Autowired
	private EptsCommonDimension eptsCommonDimension;
	
	@Autowired
	private PregnantIndicators pregnantIndicators;
	
	@Autowired
	private BreastfeedingIndicators breastfeedingIndicators;
	
	public DataSetDefinition constructTxPvlsDatset() {
		
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
		String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
		dsd.setName("Tx_Pvls Data Set");
		dsd.addParameters(getParameters());
		// tie dimensions to this data definition
		dsd.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));
		dsd.addDimension("age", EptsReportUtils.map(eptsCommonDimension.age(), "effectiveDate=${endDate}"));
		dsd.addDimension("q", EptsReportUtils.map(eptsCommonDimension.maternityDimension(), mappings));
		
		dsd.addColumn("0N", "Total patients with suppressed Viral load - Numerator",
		    EptsReportUtils.map(hivIndicators.patientsWithViralLoadSuppression(), mappings), "");
		
		dsd.addColumn("0D", "Total patients with Viral load - Denominator",
		    EptsReportUtils.map(hivIndicators.patientsWithViralLoadBetweenDates(), mappings), "");
		
		// constructing the first row of pregnant and breast feeding mothers
		
		dsd.addColumn("1N", "Pregnant Women - Numerator",
		    EptsReportUtils.map(pregnantIndicators.getPregnantWomenWithSuppressedViralLoadIn12Months(), mappings), "q=pregnant");
		
		dsd.addColumn("1D", "Pregnant Women - Denominator",
		    EptsReportUtils.map(pregnantIndicators.getPregnantWomenWithViralLoadIn12Months(), mappings), "q=pregnant");
		
		// constructing the rows for breastfeeding women
		
		dsd.addColumn("2N", "Breastfeeding - Women Numerator",
		    EptsReportUtils.map(breastfeedingIndicators.getBreastfeedingWomenWithSuppressedViralLoadIn12Months(), mappings), "");
		
		dsd.addColumn("2D", "Breastfeeding - Women Denominator",
		    EptsReportUtils.map(breastfeedingIndicators.getBreastfeedingWomenWithViralLoadIn12Months(), mappings), "");
		
		// constructing the rows for children
		// Numerator
		dsd.addColumn("3N-01", "Children Numerator (Under 1 year) ",
		    EptsReportUtils.map(hivIndicators.patientsWithViralLoadSuppression(), mappings), "age=<1");
		dsd.addColumn("3N-02", "Children Numerator (1-4 years) ",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(1, 4), mappings), "");
		dsd.addColumn("3N-03", "Children Numerator (5 - 9 years) ",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(5, 9), mappings), "");
		
		// denominator
		dsd.addColumn("3D-01", "Children Denominator (Under 1 year) ",
		    EptsReportUtils.map(hivIndicators.patientsWithViralLoadBetweenDates(), mappings), "age=<1");
		dsd.addColumn("3D-02", "Children Denominator (1-4 years) ",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(1, 4), mappings), "");
		dsd.addColumn("3D-03", "Children Denominator (5 - 9 years) ",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(5, 9), mappings), "");
		
		// constructing for adults
		// Numerator
		dsd.addColumn("4N-01", "Adults with suppressed VL Numerator (10-14 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(10, 14), mappings), "gender=M");
		dsd.addColumn("4N-02", "Adults with suppressed VL Numerator (10-14 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(10, 14), mappings), "gender=F");
		dsd.addColumn("4N-04", "Adults with suppressed VL Numerator (15-19 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(15, 19), mappings), "gender=M");
		dsd.addColumn("4N-05", "Adults with suppressed VL Numerator (15-19 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(15, 19), mappings), "gender=F");
		dsd.addColumn("4N-07", "Adults with suppressed VL Numerator (20-24 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(20, 24), mappings), "gender=M");
		dsd.addColumn("4N-08", "Adults with suppressed VL Numerator (20-24 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(20, 24), mappings), "gender=F");
		dsd.addColumn("4N-10", "Adults with suppressed VL Numerator (25-29 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(25, 29), mappings), "gender=M");
		dsd.addColumn("4N-11", "Adults with suppressed VL Numerator (25-29 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(25, 29), mappings), "gender=F");
		dsd.addColumn("4N-13", "Adults with suppressed VL Numerator (30-34 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(30, 34), mappings), "gender=M");
		dsd.addColumn("4N-14", "Adults with suppressed VL Numerator (30-34 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(30, 34), mappings), "gender=F");
		dsd.addColumn("4N-16", "Adults with suppressed VL Numerator (35-39 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(35, 39), mappings), "gender=M");
		dsd.addColumn("4N-17", "Adults with suppressed VL Numerator (35-39 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(35, 39), mappings), "gender=F");
		dsd.addColumn("4N-19", "Adults with suppressed VL Numerator (40-44 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(40, 44), mappings), "gender=M");
		dsd.addColumn("4N-20", "Adults with suppressed VL Numerator (40-44 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(40, 44), mappings), "gender=F");
		dsd.addColumn("4N-22", "Adults with suppressed VL Numerator (45-49 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(45, 49), mappings), "gender=M");
		dsd.addColumn("4N-23", "Adults with suppressed VL Numerator (45-49 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(45, 49), mappings), "gender=F");
		dsd.addColumn("4N-25", "Adults with suppressed VL Numerator (50+ years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(50, 200), mappings), "gender=M");
		dsd.addColumn("4N-26", "Adults with suppressed VL Numerator (50+ years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadSuppressionWithinAgeBracket(50, 200), mappings), "gender=F");
		
		// denominator
		
		dsd.addColumn("4D-01", "Adults with VL Denominator (10-14 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(10, 14), mappings), "gender=M");
		dsd.addColumn("4D-02", "Adults with VL Denominator (10-14 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(10, 14), mappings), "gender=F");
		dsd.addColumn("4D-04", "Adults with VL Denominator (15-19 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(15, 19), mappings), "gender=M");
		dsd.addColumn("4D-05", "Adults with VL Denominator (15-19 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(15, 19), mappings), "gender=F");
		dsd.addColumn("4D-07", "Adults with VL Denominator (20-24 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(20, 24), mappings), "gender=M");
		dsd.addColumn("4D-08", "Adults with VL Denominator (20-24 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(20, 24), mappings), "gender=F");
		dsd.addColumn("4D-10", "Adults with VL Denominator (25-29 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(25, 29), mappings), "gender=M");
		dsd.addColumn("4D-11", "Adults with VL Denominator (25-29 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(25, 29), mappings), "gender=F");
		dsd.addColumn("4D-13", "Adults with VL Denominator (30-34 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(30, 34), mappings), "gender=M");
		dsd.addColumn("4D-14", "Adults with VL Denominator (30-34 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(30, 34), mappings), "gender=F");
		dsd.addColumn("4D-16", "Adults with VL Denominator (35-39 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(35, 39), mappings), "gender=M");
		dsd.addColumn("4D-17", "Adults with VL Denominator (35-39 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(35, 39), mappings), "gender=F");
		dsd.addColumn("4D-19", "Adults with VL Denominator (40-44 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(40, 44), mappings), "gender=M");
		dsd.addColumn("4D-20", "Adults with VL Denominator (40-44 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(40, 44), mappings), "gender=F");
		dsd.addColumn("4D-22", "Adults with VL Denominator (45-49 years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(45, 49), mappings), "gender=M");
		dsd.addColumn("4D-23", "Adults with VL Denominator (45-49 years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(45, 49), mappings), "gender=F");
		dsd.addColumn("4D-25", "Adults with VL Denominator (50+ years males)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(50, 200), mappings), "gender=M");
		dsd.addColumn("4D-26", "Adults with VL Denominator (50+ years females)",
		    EptsReportUtils.map(hivIndicators.getPatientsWithViralLoadResultsWithinAgeBracket(50, 200), mappings), "gender=F");
		return dsd;
		
	}
}
