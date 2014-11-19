/**
 * @(#) JsoupParserTest.java
 *
 * This file is part of the Course Scheduler, an open source, cross platform
 * course scheduling tool, configurable for most universities.
 *
 * Copyright (C) 2010-2014 Devyse.io; All rights reserved.
 *
 * @license GNU General Public License version 3 (GPLv3)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package io.devyse.scheduler.parse.jsoup;

import io.devyse.scheduler.parse.jsoup.banner.CourseSearchParser;
import io.devyse.scheduler.parse.jsoup.banner.CourseSelectionParser;
import io.devyse.scheduler.parse.jsoup.banner.TermSelectionParser;
import io.devyse.scheduler.retrieval.JOptionPaneSelector;

import java.util.concurrent.ForkJoinPool;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import Scheduler.LegacyDataModelPersistor;

/**
 * @author Mike Reinhold
 *
 */
public class JsoupParserTest {

	public static void main(String[] args){

		//Make sure the majority of SSL/TLS protocols are enabled
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3,SSLv2Hello");
		
		
		String startURL = "https://jweb.kettering.edu/cku1/xhwschedule.P_SelectSubject";
		
		ForkJoinPool pool = new ForkJoinPool();
		
		try {
			TermSelectionParser termSelect = new TermSelectionParser(Jsoup.connect(startURL).method(Method.GET).execute().parse(), new JOptionPaneSelector());
			CourseSelectionParser courseSelect = new CourseSelectionParser(pool.invoke(termSelect));
			CourseSearchParser courseParse = new CourseSearchParser(pool.invoke(courseSelect), new LegacyDataModelPersistor());
			
			pool.invoke(courseParse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}
