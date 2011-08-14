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

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gatein.api.rest.resource.content.CategoryResource;
import org.gatein.api.rest.resource.content.ManagedContentResource;
import org.gatein.api.rest.resource.content.ManagedContentsResource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class ManagedContentTestCase extends ApiTestCase
{

   static String testCategory = CategoriesTest.categoriesURL + "/testCategory";
   static String testManagedContents = testCategory + "/managedcontent";
   
   @BeforeMethod
   public void setup() throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpHead httphead = new HttpHead(testCategory);
      
      HttpResponse responseExists = client.execute(httphead);
      if (responseExists.getStatusLine().getStatusCode() == Status.NOT_FOUND.getStatusCode())
      {
         HttpPost httpPost = new HttpPost(CategoriesTest.categoriesURL);
         httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML);
         StringEntity stringEntity = new StringEntity("<category><name>testCategory</name></category>");
         httpPost.setEntity(stringEntity);

         HttpResponse createResponse = client.execute(httpPost);
         Assert.assertEquals(createResponse.getStatusLine().getStatusCode(), Status.CREATED.getStatusCode());
         createResponse.getEntity().getContent().close();
      }
   }
   
   @AfterMethod
   public void tearDown() throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpHead httphead = new HttpHead(testCategory);
      
      HttpResponse responseExists = client.execute(httphead);
      if (responseExists.getStatusLine().getStatusCode() != Status.NOT_FOUND.getStatusCode())
      {
         HttpDelete httpDelete = new HttpDelete(testCategory);
         HttpResponse response = client.execute(httpDelete);
         
         Assert.assertTrue(isSuccess(response));
      }
   }
   
   @Test
   public void testGetManagedContentsAsAppXML() throws Exception
   {
      System.out.println(testManagedContents);
      HttpResponse response = getResponseForAcceptHeader(testManagedContents, MediaType.APPLICATION_XML);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
      
      //since we have just created this category, it will be empty of managed contents
      ManagedContentsResource managedContentsResource = getManagedContentsResource(response);
      Assert.assertNotNull(managedContentsResource.getManagedContents());
      Assert.assertTrue(managedContentsResource.getManagedContents().isEmpty());
      //check the self link is the url used in the get request
      Assert.assertEquals(managedContentsResource.getSelfLink().getHref(), testManagedContents);
   }
   
   @Test
   public void testAddManagedContents() throws Exception
   {
      HttpResponse response = postResponseForAcceptHeader(testManagedContents, MediaType.APPLICATION_XML);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 400);
      
      HttpClient client = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(testManagedContents);
      httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML);
      
      String managedResource = "";
      httpPost.setEntity(new StringEntity(managedResource));
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      managedResource = "<managedcontent><name>test</name></managedcontent>";
      httpPost.setEntity(new StringEntity(managedResource));
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      managedResource = "<managedcontent><name>test</name><link rel=\"gatein.content.portlet.invalid-rel\" href=\"http://localhost:8080/rest/api/contentregistry/contents/portlets/web/IFramePortlet\"/></managedcontent>";
      httpPost.setEntity(new StringEntity(managedResource));
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      managedResource = "<managedcontent><name>test</name><link rel=\"gatein.content.portlet\" href=\"http://localhost:8080/rest/api/contentregistry/contents/portlets/web/invalidPortleName\"/></managedcontent>";
      httpPost.setEntity(new StringEntity(managedResource));
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      managedResource = "<managedcontent><name>test</name><link rel=\"gatein.content.portlet\" href=\"http://localhost:8080/rest/api/badurl/contents/portlets/web/IFramePortlet\"/></managedcontent>";
      httpPost.setEntity(new StringEntity(managedResource));
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      managedResource = "<managedcontent><name>test</name><link rel=\"gatein.content.portlet\" href=\"http://localhost:8080/rest/api/contentregistry/contents/portlets/web/IFramePortlet\"/></managedcontent>";
      httpPost.setEntity(new StringEntity(managedResource));
      response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.CREATED.getStatusCode());
      response.getEntity().getContent().close();
   }
   

   @Test
   public void testDeleteManagedContents() throws Exception
   {
      //create a managed resource first
      HttpClient client = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(testManagedContents);
      httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML);
      String managedResource = "<managedcontent><name>test</name><link rel=\"gatein.content.portlet\" href=\"http://localhost:8080/rest/api/contentregistry/contents/portlets/web/IFramePortlet\"/></managedcontent>";
      httpPost.setEntity(new StringEntity(managedResource));
      HttpResponse response = client.execute(httpPost);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.CREATED.getStatusCode());
      Header contentLocation = response.getFirstHeader(HttpHeaders.CONTENT_LOCATION);
      Assert.assertNotNull(contentLocation);
      Assert.assertNotNull(contentLocation.getValue());
      response.getEntity().getContent().close();
      
      HttpGet httpGet = new HttpGet(contentLocation.getValue());
      httpGet.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML);
      HttpResponse getResponse = client.execute(httpGet);
      Assert.assertEquals(getResponse.getStatusLine().getStatusCode(), Status.OK.getStatusCode());
      ManagedContentResource managedContent = getManagedContentResource(getResponse);

      Assert.assertEquals(managedContent.getName(), "test");
      Assert.assertEquals(managedContent.getSelfLink().getHref(), testManagedContents + "/test");
      //TODO: test the object more
      
      //try to delete the resource now...
      HttpDelete httpDelete = new HttpDelete(testManagedContents + "/test");
      HttpResponse deleteResponse = client.execute(httpDelete);
      Assert.assertEquals(deleteResponse.getStatusLine().getStatusCode(), Status.OK.getStatusCode());
      deleteResponse.getEntity().getContent().close();
      
      httpGet = new HttpGet(testManagedContents + "/test");
      getResponse = client.execute(httpGet);
      Assert.assertEquals(getResponse.getStatusLine().getStatusCode(), Status.NOT_FOUND.getStatusCode());
   }
   
   @Test
   public void testUpdateManagedContent() throws Exception
   {
      
   }
   
   protected ManagedContentResource getManagedContentResource(HttpResponse response) throws Exception
   {
      JAXBContext context = JAXBContext.newInstance(ManagedContentResource.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      return (ManagedContentResource) unmarshaller.unmarshal(response.getEntity().getContent());
   }
   
   protected ManagedContentsResource getManagedContentsResource(HttpResponse response) throws Exception
   {
      JAXBContext context = JAXBContext.newInstance(ManagedContentsResource.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      return (ManagedContentsResource) unmarshaller.unmarshal(response.getEntity().getContent());
   }
}

