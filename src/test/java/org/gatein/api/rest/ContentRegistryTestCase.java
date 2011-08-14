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

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.gatein.api.rest.resource.APIRootResource;
import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.content.CategoriesResource;
import org.gatein.api.rest.resource.content.ContentRegistryResource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class ContentRegistryTestCase extends ApiTestCase
{

   static String contentRegistryURL = rootURL + "/contentregistry";
   
   @Test
   public void testGetContentRegistryResourceAsAppXML() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(contentRegistryURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isSuccess(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
      
      ContentRegistryResource contentRegistryResource = getContentRegistryResource(response);
      verifyContentRegistryResource(contentRegistryResource);
   }
 
   @Test
   public void testPutContentRegistryResourceAsAppXML() throws Exception
   {
      HttpResponse response = putResponseForAcceptHeader(contentRegistryURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 405);
   }
   
   @Test
   public void testPostContentRegistryResourceAsAppXML() throws Exception
   {
      HttpResponse response = postResponseForAcceptHeader(contentRegistryURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 405);
   }
   
   @Test
   public void testDeleteContentRegistryResourceAsAppXML() throws Exception
   {
      HttpResponse response = deleteResponseForAcceptHeader(contentRegistryURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 405);
   }
   
   @Test
   public void testGetContentRegistryResourceAsJSON() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(contentRegistryURL, MediaType.APPLICATION_JSON);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }
   
   @Test
   public void testGetContentRegistryResourceAsTextXML() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(contentRegistryURL, MediaType.TEXT_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }
   
   @Test
   public void testGetContentRegistryResourceAsPlainText() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(contentRegistryURL, MediaType.TEXT_PLAIN);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }
   
   protected ContentRegistryResource getContentRegistryResource(HttpResponse response) throws Exception
   {
      JAXBContext context = JAXBContext.newInstance(ContentRegistryResource.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      return (ContentRegistryResource)unmarshaller.unmarshal(response.getEntity().getContent());
   }

   public void verifyContentRegistryResource(ContentRegistryResource contentRegistryResource) throws Exception
   {
      Assert.assertNotNull(contentRegistryResource);
      
      Link selfLink = new Link("self", contentRegistryURL);
      Assert.assertEquals(contentRegistryResource.getLinks().size(), 3);
      Assert.assertTrue(containsLink(contentRegistryResource.getLinks(), selfLink));

      Link categoriesLink = new Link(CategoriesResource.LINK_REL, CategoriesTest.categoriesURL);
      Assert.assertTrue(containsLink(contentRegistryResource.getLinks(), categoriesLink));
      //verify we can retrieve a successful response from the link
      HttpResponse categoriesResponse = getResponseForAcceptHeader(categoriesLink.getHref(), MediaType.APPLICATION_XML);
      Assert.assertTrue(isSuccess(categoriesResponse));
      
      Link contentsLink = new Link("gatein.contents", contentRegistryURL + "/contents");
      Assert.assertTrue(containsLink(contentRegistryResource.getLinks(), contentsLink));
      //verify we can retrieve a successful response from the link
      HttpResponse contentsResponse = getResponseForAcceptHeader(contentsLink.getHref(), MediaType.APPLICATION_XML);
      Assert.assertTrue(isSuccess(contentsResponse));
   }
}

