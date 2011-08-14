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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gatein.api.rest.resource.content.CategoriesResource;
import org.gatein.api.rest.resource.content.CategoryResource;
import org.gatein.api.rest.resource.content.ManagedContentsResource;
import org.gatein.api.rest.service.CategoriesService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class CategoryTest extends ApiTestCase
{

   static String testCategoryExists = CategoriesTest.categoriesURL + "/testCategoryA";

   static String testCategoryDoesntExist = CategoriesTest.categoriesURL + "/testCategoryB";

   @BeforeMethod
   public void setup() throws Exception
   {
      //create testCategoryExists if it doesn't currently exist
      HttpClient client = new DefaultHttpClient();
      HttpHead httpheadExists = new HttpHead(testCategoryExists);

      HttpResponse responseExists = client.execute(httpheadExists);
      //responseExists.getEntity().getContent().close();
      if (responseExists.getStatusLine().getStatusCode() == Status.NOT_FOUND.getStatusCode())
      {
         HttpPost httpPost = new HttpPost(CategoriesTest.categoriesURL);
         httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML);
         StringEntity stringEntity = new StringEntity("<category><name>testCategoryA</name></category>");
         httpPost.setEntity(stringEntity);

         HttpResponse createResponse = client.execute(httpPost);
         Assert.assertEquals(createResponse.getStatusLine().getStatusCode(), Status.CREATED.getStatusCode());
         createResponse.getEntity().getContent().close();
      }

      //delete testCategoryDoesntExist if it doesn't current exist
      HttpHead httpHeadDoesntExist = new HttpHead(testCategoryDoesntExist);
      HttpResponse responseDoesntExist = client.execute(httpHeadDoesntExist);
      //responseDoesntExist.getEntity().getContent().close();
      System.out.println(responseDoesntExist.getStatusLine().getStatusCode());
      if (responseDoesntExist.getStatusLine().getStatusCode() != Status.NOT_FOUND.getStatusCode())
      {
         HttpDelete httpDelete = new HttpDelete(testCategoryDoesntExist);

         HttpResponse httpDeleteResponse = client.execute(httpDelete);
         Assert.assertEquals(httpDeleteResponse.getStatusLine().getStatusCode(), Status.OK);
      }

   }

   @Test
   public void testGetCategoryAsAppXML() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(testCategoryExists, MediaType.APPLICATION_XML);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);

      CategoryResource resource = getCategoryResource(response);
      Assert.assertEquals(resource.getName(), "testCategoryA");
      Assert.assertEquals(resource.getDisplayName(), "testCategoryA");
      Assert.assertEquals(resource.getDescription(), null);
      Assert.assertEquals(resource.getId(), "testCategoryA");

      HttpResponse responseDoesntExist = getResponseForAcceptHeader(testCategoryDoesntExist, MediaType.APPLICATION_XML);
      Assert.assertEquals(responseDoesntExist.getStatusLine().getStatusCode(), Status.NOT_FOUND.getStatusCode());
   }

   @Test
   public void testPostCategoryResource() throws Exception
   {
      HttpResponse response = postResponseForAcceptHeader(testCategoryExists, MediaType.APPLICATION_XML);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 405);

      response = postResponseForAcceptHeader(testCategoryDoesntExist, MediaType.APPLICATION_XML);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 405);
   }

   @Test
   public void testPutCategoryResourceAsAppXMLNoContent() throws Exception
   {
      //test trying to put without sending across an object
      HttpResponse response = putResponseForAcceptHeader(testCategoryExists, MediaType.APPLICATION_XML);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());

      response = putResponseForAcceptHeader(testCategoryDoesntExist, MediaType.APPLICATION_XML);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.NOT_FOUND.getStatusCode());
   }

   @Test
   public void testPutCategoryResource() throws Exception
   {
      HttpClient client = new DefaultHttpClient();
      HttpPut httpPut = new HttpPut(testCategoryExists);
      httpPut.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML);

      HttpResponse response = client.execute(httpPut);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();

      StringEntity stringEntity = new StringEntity("I am not a valid category");
      httpPut.setEntity(stringEntity);
      response = client.execute(httpPut);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();

      httpPut.setEntity(null);
      response = client.execute(httpPut);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();

      stringEntity = new StringEntity("");
      httpPut.setEntity(stringEntity);
      response = client.execute(httpPut);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();

      //this is still bad, since we are doing an update, we need for the category to contain the same information
      stringEntity = new StringEntity("<category><name>testCategoryA</name></category>");
      httpPut.setEntity(stringEntity);
      response = client.execute(httpPut);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();

      stringEntity = new StringEntity("<category><name>testCategoryA</name><id>testCategoryA</id></category>");
      httpPut.setEntity(stringEntity);
      response = client.execute(httpPut);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();

      //invalid href link added, the link should not be editable
      String categoryString = "<category><name>testCategoryA</name><id>testCategoryA</id>" +
      "<link rel=\"" + ManagedContentsResource.LINK_REL + "\" href=\"" + testCategoryExists + "/invalid\"/>" +
      "</category>";
      stringEntity = new StringEntity(categoryString);
      httpPut.setEntity(stringEntity);
      response = client.execute(httpPut);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.BAD_REQUEST.getStatusCode());
      response.getEntity().getContent().close();
      
      //check valid results, and make sure the order doesn't matter
      
      //check with no changes
      categoryString = "<category><name>testCategoryA</name><id>testCategoryA</id>" +
      "<link rel=\"" + ManagedContentsResource.LINK_REL + "\" href=\"" + testCategoryExists + "/managedcontent\"/>" +
      "<display-name>testCategoryA</display-name></category>";
      stringEntity = new StringEntity(categoryString);
      httpPut.setEntity(stringEntity);
      response = client.execute(httpPut);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.OK.getStatusCode());
      response.getEntity().getContent().close();
      //get the object, and make sure things are sill as expected
      HttpResponse getResponse = getResponseForAcceptHeader(testCategoryExists, MediaType.APPLICATION_XML);
      checkExpectedResource(getCategoryResource(getResponse));
      
      categoryString = "<category><id>testCategoryA</id><name>testCategoryA</name>" +
      "<link rel=\"" + ManagedContentsResource.LINK_REL + "\" href=\"" + testCategoryExists + "/managedcontent\"/>" +
      "<display-name>testCategoryA</display-name></category>";
      stringEntity = new StringEntity(categoryString);
      httpPut.setEntity(stringEntity);
      response = client.execute(httpPut);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.OK.getStatusCode());
      response.getEntity().getContent().close();
      //get the object, and make sure things are sill as expected
      getResponse = getResponseForAcceptHeader(testCategoryExists, MediaType.APPLICATION_XML);
      checkExpectedResource(getCategoryResource(getResponse));
      
      categoryString = "<category>" +
      "<link rel=\"" + ManagedContentsResource.LINK_REL + "\" href=\"" + testCategoryExists + "/managedcontent\"/>" +
      "<id>testCategoryA</id><display-name>testCategoryA</display-name><name>testCategoryA</name></category>";
      stringEntity = new StringEntity(categoryString);
      httpPut.setEntity(stringEntity);
      response = client.execute(httpPut);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.OK.getStatusCode());
      response.getEntity().getContent().close();
      //get the object, and make sure things are sill as expected
      getResponse = getResponseForAcceptHeader(testCategoryExists, MediaType.APPLICATION_XML);
      checkExpectedResource(getCategoryResource(getResponse));
      
      //check now with changes. The category object only accepts changes to the Display-Name and Description
      categoryString = "<category>" +
      "<link rel=\"" + ManagedContentsResource.LINK_REL + "\" href=\"" + testCategoryExists + "/managedcontent\"/>" +
      "<id>testCategoryA</id><display-name>newdisplayname</display-name><name>testCategoryA</name>" +
      "<description>This is a new description</description></category>";
      stringEntity = new StringEntity(categoryString);
      httpPut.setEntity(stringEntity);
      response = client.execute(httpPut);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), Status.OK.getStatusCode());
      response.getEntity().getContent().close();
      //get the object, and make sure things are sill as expected
      getResponse = getResponseForAcceptHeader(testCategoryExists, MediaType.APPLICATION_XML);
      CategoryResource resource = getCategoryResource(getResponse);
      Assert.assertEquals(resource.getName(), "testCategoryA");
      Assert.assertEquals(resource.getId(), "testCategoryA");
      Assert.assertEquals(resource.getDisplayName(), "newdisplayname");
      Assert.assertEquals(resource.getDescription(), "This is a new description");
   }

   @Test
   public void testDeleteCategoryResourceAsAppXML() throws Exception
   {
      HttpResponse responseExists = deleteResponseForAcceptHeader(testCategoryExists, MediaType.APPLICATION_XML);
      Assert.assertEquals(responseExists.getStatusLine().getStatusCode(), Status.OK.getStatusCode());
      //Check that we can't retrieve the resource now
      HttpResponse getResponse = getResponseForAcceptHeader(testCategoryExists, MediaType.APPLICATION_XML);
      Assert.assertEquals(getResponse.getStatusLine().getStatusCode(), Status.NOT_FOUND.getStatusCode());
      

      HttpResponse responseDoesntExists = deleteResponseForAcceptHeader(testCategoryDoesntExist,
            MediaType.APPLICATION_XML);
      Assert.assertEquals(responseDoesntExists.getStatusLine().getStatusCode(), Status.NOT_FOUND.getStatusCode());
   }

   @Test
   public void testGetCategoryResourceAsJSON() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(testCategoryExists, MediaType.APPLICATION_JSON);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }

   @Test
   public void testGetCategoryResourceAsTextXML() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(testCategoryExists, MediaType.TEXT_XML);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }

   @Test
   public void testGetCategoryResourceAsPlainText() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(testCategoryExists, MediaType.TEXT_PLAIN);
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }

   protected CategoryResource getCategoryResource(HttpResponse response) throws Exception
   {
      JAXBContext context = JAXBContext.newInstance(CategoryResource.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      return (CategoryResource) unmarshaller.unmarshal(response.getEntity().getContent());
   }
   
   protected void checkExpectedResource(CategoryResource resource)
   {
      Assert.assertEquals(resource.getName(), "testCategoryA");
      Assert.assertEquals(resource.getId(), "testCategoryA");
      Assert.assertEquals(resource.getDisplayName(), "testCategoryA");
      Assert.assertNull(resource.getDescription());
   }

}
