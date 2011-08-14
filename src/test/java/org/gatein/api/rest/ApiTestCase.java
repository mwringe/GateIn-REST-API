/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2011, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.gatein.api.rest;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.content.CategoryResource;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class ApiTestCase
{

   protected static String rootURL = "http://localhost:8080/rest/api";
   
   protected HttpResponse getResponseForAcceptHeader(String url, String mediaType) throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpGet httpGet = new HttpGet(url);
      httpGet.addHeader(HttpHeaders.ACCEPT, mediaType);
      return client.execute(httpGet);
   }
   
   protected HttpResponse putResponseForAcceptHeader(String url, String mediaType) throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpPut httpPut = new HttpPut(url);
      httpPut.addHeader(HttpHeaders.ACCEPT, mediaType);
      return client.execute(httpPut);
   }
   
   protected HttpResponse postResponseForAcceptHeader(String url, String mediaType) throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(url);
      httpPost.addHeader(HttpHeaders.ACCEPT, mediaType);
      return client.execute(httpPost);
   }
   
   protected HttpResponse deleteResponseForAcceptHeader(String url, String mediaType) throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpDelete httpDelete = new HttpDelete(url);
      httpDelete.addHeader(HttpHeaders.ACCEPT, mediaType);
      return client.execute(httpDelete);
   }
   
   protected boolean isClientError(HttpResponse response)
   {
      return (response.getStatusLine().getStatusCode() >= 400 && response.getStatusLine().getStatusCode() < 500);
   }
   
   protected boolean isServerError(HttpResponse response)
   {
      return (response.getStatusLine().getStatusCode() >= 500 && response.getStatusLine().getStatusCode() < 600);
   }
   
   protected boolean isSuccess(HttpResponse response)
   {
      return (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300);
   }
   
   protected boolean isRedirect(HttpResponse response)
   {
      return (response.getStatusLine().getStatusCode() >= 300 && response.getStatusLine().getStatusCode() < 400);
   }
   
   protected boolean isInformational(HttpResponse response)
   {
      return (response.getStatusLine().getStatusCode() >= 100 && response.getStatusLine().getStatusCode() < 200);
   }
   
   protected boolean containsLink(Collection<Link> links, Link targetLink)
   {
      for (Link link : links)
      {
         if (link.equals(targetLink))
         {
            return true;
         }
      }
      
      return false;
   }
}

