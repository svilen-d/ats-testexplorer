/*
 * Copyright 2017 Axway Software
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
package com.axway.ats.testexplorer.pages.runsByTypeDashboard.suite;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.axway.ats.log.autodb.exceptions.DatabaseAccessException;
import com.axway.ats.testexplorer.pages.BasePage;
import com.axway.ats.testexplorer.pages.WelcomePage;
import com.axway.ats.testexplorer.pages.runsByTypeDashboard.home.RunsByTypeDashboardHomePage;
import com.axway.ats.testexplorer.pages.runsByTypeDashboard.run.RunsByTypeDashboardRunPage;

public class RunsByTypeDashboardSuitePage extends BasePage {

    private static final long serialVersionUID = 1L;

    private String[]          jsonDatas;

    public RunsByTypeDashboardSuitePage( PageParameters parameters ) {

        super( parameters );

        addNavigationLink( WelcomePage.class, new PageParameters(), "Home", null );
        addNavigationLink( RunsByTypeDashboardHomePage.class,
                           new PageParameters(),
                           "Runs by type",
                           parameters.get( "productName" ).toString() + "/"
                                   + parameters.get( "versionName" ).toString() + "/"
                                   + parameters.get( "type" ).toString() );
        addNavigationLink( RunsByTypeDashboardRunPage.class,
                           parameters,
                           "Suites",
                           parameters.get( "suiteName" ).toString() );

        AjaxLink<String> modalTooltip = new AjaxLink<String>( "modalTooltip" ) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(
                                 AjaxRequestTarget target ) {

            }
        };
        //        modalTooltip.
        modalTooltip.add( new WebMarkupContainer( "helpButton" ) );

        add( modalTooltip );

        if( !parameters.isEmpty() ) {
            try {
                jsonDatas = new DashboardSuiteUtils().initData(
                                                               parameters.get( "suiteName" ).toString(),
                                                               parameters.get( "type" ).toString(),
                                                               parameters.get( "suiteBuild" ).toString(),
                                                               parameters.get( "productName" ).toString(),
                                                               parameters.get( "versionName" ).toString() );
            } catch( DatabaseAccessException e ) {
                LOG.error( "Unable to get testcases data.", e );
                error( "Unable to get testcases data." );
            } 

        }
    }

    @Override
    public void renderHead(
                            IHeaderResponse response ) {

        if( !getPageParameters().isEmpty() ) {
            new DashboardSuiteUtils().callJavaScript( response, jsonDatas );
        } else {
            String errorScript = ";resize();";
            response.render( OnLoadHeaderItem.forScript( errorScript ) );
        }
    }

    @Override
    public String getPageName() {

        return "Testcases";
    }

}
