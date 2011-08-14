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

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.gatein.api.rest.resource.APIRootResource;
import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.content.CategoryResource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class ApiRootTestCase extends ApiTestCase
{
   /* TODO:
    * - test headers more
    * 
    * NOTE: maybe we should test against a Document instead of against an unmarshalled object?
    */
   
   @Test
   public void testGetRootResourceAsAppXML() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(this.rootURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isSuccess(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);
      
      APIRootResource rootResource = getRootResource(response);
      verifyRootResource(rootResource);
   }
   
   @Test
   public void testPutRootResourceAsAppXML() throws Exception
   {
      HttpResponse response = putResponseForAcceptHeader(this.rootURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 405);
   }
   
   @Test
   public void testPostRootResourceAsAppXML() throws Exception
   {
      HttpResponse response = postResponseForAcceptHeader(this.rootURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 405);
   }
   
   @Test
   public void testDeleteRootResourceAsAppXML() throws Exception
   {
      HttpResponse response = putResponseForAcceptHeader(this.rootURL, MediaType.APPLICATION_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 405);
   }
   
   @Test
   public void testGetRootResourceAsJSON() throws Exception
   {
      //TODO: we will be supporting json in a future version, then this test will need to be written to test that it works
      HttpResponse response = getResponseForAcceptHeader(this.rootURL, MediaType.APPLICATION_JSON);
      System.out.println(response.getStatusLine().getStatusCode());
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }
   
   @Test
   public void testGetRootResourceAsPlainText() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(this.rootURL, MediaType.TEXT_PLAIN);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }
   
   @Test
   public void testGetRootResoruceAsTextXML() throws Exception
   {
      HttpResponse response = getResponseForAcceptHeader(this.rootURL, MediaType.TEXT_XML);
      Assert.assertTrue(isClientError(response));
      Assert.assertEquals(response.getStatusLine().getStatusCode(), 406);
   }
   
   protected APIRootResource getRootResource(HttpResponse response) throws Exception
   {
      JAXBContext context = JAXBContext.newInstance(APIRootResource.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      return (APIRootResource)unmarshaller.unmarshal(response.getEntity().getContent());
   }
   
   protected void verifyRootResource(APIRootResource rootResource) throws Exception
   {
      Assert.assertNotNull(rootResource);
      
      Link selfLink = new Link("self", this.rootURL);
      Assert.assertEquals(rootResource.getLinks().size(), 5);
      Assert.assertTrue(containsLink(rootResource.getLinks(), selfLink));
      
      //verify the documentation link
      Link documentLink = new Link("gatein.documenation", this.rootURL + "/documentation");
      Assert.assertTrue(containsLink(rootResource.getLinks(), documentLink));
      //verify we can retrieve a successful response from the link
      HttpResponse documentsResponse = getResponseForAcceptHeader(documentLink.getHref(), MediaType.APPLICATION_XML);
      Assert.assertTrue(isSuccess(documentsResponse));
      
      //verify the pages link
      Link pagesLink = new Link("gatein.pages", this.rootURL + "/pages");
      Assert.assertTrue(containsLink(rootResource.getLinks(), pagesLink));
      //verify we can retrieve a successful response from the link
      HttpResponse pagesResponse = getResponseForAcceptHeader(pagesLink.getHref(), MediaType.APPLICATION_XML);
      Assert.assertTrue(isSuccess(pagesResponse));
      
      //verify the contentregistry link is what we expect
      Link contentregistryLink = new Link("gatein.contentregistry", ContentRegistryTestCase.contentRegistryURL);
      Assert.assertTrue(containsLink(rootResource.getLinks(), contentregistryLink));
      //verify we can retrieve a successful response from the link
      HttpResponse contentRegistryResponse = getResponseForAcceptHeader(contentregistryLink.getHref(), MediaType.APPLICATION_XML);
      Assert.assertTrue(isSuccess(contentRegistryResponse));
      
      //verify the sites link
      Link sitesLink = new Link("gatein.sites", this.rootURL + "/sites");
      Assert.assertTrue(containsLink(rootResource.getLinks(), sitesLink));
      //verify we can retrieve a successful response from the link
      HttpResponse sitesResponse = getResponseForAcceptHeader(sitesLink.getHref(), MediaType.APPLICATION_XML);
      Assert.assertTrue(isSuccess(sitesResponse));
   }
}

