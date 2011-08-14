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

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gatein.api.rest.resource.content.CategoriesResource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class CategoriesTest extends ApiTestCase
{
   static String categoriesURL = rootURL + "/contentregistry" + "/categories";

   @Test
   public void testGetContentRegistryResourceAsAppXML() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(categoriesURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isSuccess(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
      
      CategoriesResource categoriesResource = getCategoriesResource(response);
      verifyCategoriesResource(categoriesResource);
   }
   
   @Test
   public void testPostCategoriesResourceNoContent() throws Exception
   {
      HttpResponse response = postResponseForAcceptHeader(categoriesURL, MediaType.APPLICATION_XML);
      System.out.println(response.getStatusLine().getStatusCode());
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
   }
   
   @Test
   public void testPostCategoriesResourceInvalidContent() throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(categoriesURL);
      httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML);
      
      HttpResponse response = client.execute(httpPost);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      StringEntity stringEntity = new StringEntity("I am not a valid category");
      httpPost.setEntity(stringEntity);
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      stringEntity = new StringEntity("<category>still not a valid category</category>");
      httpPost.setEntity(stringEntity);
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      stringEntity = new StringEntity("<category><description>getting closer, but missing name</description></category>");
      httpPost.setEntity(stringEntity);
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      stringEntity = new StringEntity("<category><description>description</description><display-name>displayname</display-name><id>why not have an id here?</id></category>");
      httpPost.setEntity(stringEntity);
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      stringEntity = new StringEntity("<category><name>close, but still not right<name></category>");
      httpPost.setEntity(stringEntity);
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
   }
   
   @Test
   public void testPostCategoriesResourceValidContent() throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(categoriesURL);
      httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML);
      StringEntity stringEntity = new StringEntity("<category><name>test123</name></category>");
      httpPost.setEntity(stringEntity);
      
      HttpResponse response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.CREATED.getStatusCode());
      Assert.assertEquals(response.getFirstHeader(HttpHeaders.LOCATION).getValue(), categoriesURL + "/test123");
      String createdCategoryURL = response.getFirstHeader(HttpHeaders.LOCATION).getValue();
      response.getEntity().getContent().close();
      
      //calling it again should fail, since POST is only for creating new objects, not updating them
      response = client.execute(httpPost);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      //delete the newly created content, so that it doesn't interfere with this test running a second time
      HttpDelete httpDelete = new HttpDelete(createdCategoryURL);
      response = client.execute(httpDelete);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.OK.getStatusCode());
   }


   @Test
   public void testPutCategoriesResourceAsAppXML() throws Exception
   {
      HttpResponse response = putResponseForAcceptHeader(categoriesURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 405);
   }
   
   @Test
   public void testDeleteCategoriesResourceAsAppXML() throws Exception
   {
      HttpResponse response = deleteResponseForAcceptHeader(categoriesURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 405);
   }
   
   @Test
   public void testGetCategoriesResourceAsJSON() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(categoriesURL, MediaType.APPLICATION_JSON);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }
   
   @Test
   public void testGetCategoriesResourceAsTextXML() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(categoriesURL, MediaType.TEXT_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }
   
   @Test
   public void testGetCategoriesResourceAsPlainText() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(categoriesURL, MediaType.TEXT_PLAIN);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }
   

   protected CategoriesResource getCategoriesResource(HttpResponse response) throws Exception
   {
      JAXBContext context = JAXBContext.newInstance(CategoriesResource.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      return (CategoriesResource)unmarshaller.unmarshal(response.getEntity().getContent());
   }

   protected void verifyCategoriesResource(CategoriesResource contentRegistryResource) throws Exception
   {
      // FIXME verifyContentRegistryResource
      
   }
   
}

