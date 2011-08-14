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
package org.gatein.api.rest.service;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.gatein.api.content.Category;
import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.content.CategoriesResource;
import org.gatein.api.rest.resource.content.CategoryResource;
import org.gatein.api.rest.resource.content.GadgetContentResource;
import org.gatein.api.rest.resource.content.ManagedContentResource;
import org.gatein.api.rest.resource.content.PortletContentResource;
import org.gatein.api.rest.resource.content.WSRPContentResource;
import org.gatein.api.rest.resource.handler.CategoriesHandler;
import org.gatein.api.rest.resource.handler.CategoryHandler;
import org.gatein.api.rest.resource.handler.ManagedContentHandler;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
@Path("/api/contentregistry/categories")
public class CategoriesService
{
   public static final String SERVICE_PATH="/api/contentregistry/categories";
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   public CategoriesResource getCategories(@Context UriInfo uriInfo)
   {  
      return generateCategoriesResource(uriInfo);
   }

   private CategoriesResource generateCategoriesResource(UriInfo uriInfo)
   {
      CategoriesHandler categoriesHandler = new CategoriesHandler(uriInfo);
      
      return categoriesHandler.getCategoriesResource();
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("/{categoryId}")
   public Response getCategory(@PathParam("categoryId") String categoryId, @Context UriInfo uriInfo)
   {
      CategoryHandler categoryHandler = new CategoryHandler(uriInfo);
      CategoryResource categoryResource = categoryHandler.getCategoryResource(categoryId);
      
      if (categoryResource == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      else
      {
         return Response.ok(categoryResource).build();
      }
      
   }
   
   @POST
   @Consumes(MediaType.APPLICATION_XML)
   public Response createCategory(@Context UriInfo uriInfo, CategoryResource categoryResource) throws URISyntaxException
   {
      
      if (categoryResource == null || categoryResource.getName() == null)
      {
         return Response.status(Status.BAD_REQUEST).build();
      }
      
      CategoryHandler categoryHandler = new CategoryHandler(uriInfo);
      
      if (categoryHandler.getCategory(categoryResource.getName()) != null)
      {
         return Response.status(Status.BAD_REQUEST).entity("Category already exists").build();
      }
      
      URI uri = categoryHandler.createCategory(categoryResource);
      
      ResponseBuilder responseBuilder = Response.created(uri);
      return responseBuilder.build();
   }
   
   @DELETE
   @Path("/{categoryId}")
   public Response deleteCategory(@PathParam("categoryId") String categoryId, @Context UriInfo uriInfo)
   {
      CategoryHandler categoryHandler = new CategoryHandler(uriInfo);
      if (categoryHandler.deleteCategory(categoryId)) //returns true if we deleted the element
      {
         return Response.ok().build();
      }
      else //returns false if the element doesn't exist 
      {
         return Response.status(Status.NOT_FOUND).build();
      }
   }
   
   @PUT
   @Path("/{categoryId}")
   public Response updateCategory(@PathParam("categoryId") String categoryId, CategoryResource categoryResource, @Context UriInfo uriInfo)
   {
      CategoryHandler categoryHandler = new CategoryHandler(uriInfo);
      
      Category category = categoryHandler.getCategory(categoryId);
      
      if (category == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      
      if (categoryResource == null)
      {
         return Response.status(Status.BAD_REQUEST).build();
      }
      
      if (categoryHandler.updateCategory(categoryResource, category))
      {
         return Response.ok().build();
      }
      else
      {
         return Response.status(Status.BAD_REQUEST).build();
      }
   }
   
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("/{categoryId}/managedcontent")
   public Response getManagedResource(@PathParam("categoryId") String categoryId, @Context UriInfo uriInfo)
   {
      ManagedContentHandler managedContentHandler = new ManagedContentHandler(uriInfo);
      
      CategoryHandler categoryHandler = new CategoryHandler(uriInfo);
      Category category = categoryHandler.getCategory(categoryId);
      
      if (category == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      else
      {
         return Response.ok(managedContentHandler.getManagedContentsResource(category)).build();
      }
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("/{categoryId}/managedcontent/{managedContentName}")
   public Response getManagedResource(@PathParam("categoryId") String categoryId, @PathParam("managedContentName") String managedContentName, @Context UriInfo uriInfo)
   {
      ManagedContentHandler managedContentHandler = new ManagedContentHandler(uriInfo);
      CategoryHandler categoryHandler = new CategoryHandler(uriInfo);
      Category category = categoryHandler.getCategory(categoryId);
      
      ManagedContentResource managedContent = managedContentHandler.getManagedContentResource(category, managedContentName);
      if (managedContent != null)
      {
         return Response.ok(managedContent).build();
      }
      else
      {
         return Response.status(Status.NOT_FOUND).build();
      }
   }
   
   @POST
   @Consumes(MediaType.APPLICATION_XML)
   @Path("/{categoryId}/managedcontent")
   public Response addManagedResource(@PathParam("categoryId") String categoryId, @Context UriInfo uriInfo, ManagedContentResource managedContentResource)
   {
      if (managedContentResource == null)
      {
         return Response.status(Status.BAD_REQUEST).build();
      }
      
      ManagedContentHandler managedContentHandler = new ManagedContentHandler(uriInfo);
      CategoryHandler categoryHandler = new CategoryHandler(uriInfo);
      Category category = categoryHandler.getCategory(categoryId);
      
      if (category == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      
      Link contentLink = managedContentResource.getContentLink();
      
      if (contentLink == null || contentLink.getHref() == null || contentLink.getRel() == null)
      {
         return Response.status(Status.BAD_REQUEST).build();
      }
      
      if (contentLink.getRel().equals(PortletContentResource.LINK_REL))
      {
         if (!contentLink.getHref().startsWith(uriInfo.getBaseUriBuilder().clone().path(PortletRespositoryService.SERVICE_PATH).build().toString()))
         {
            return Response.status(Status.BAD_REQUEST).build();
         }
         
         PortletRespositoryService portletRepositoryService = new PortletRespositoryService();
         PortletContentResource portletResource = portletRepositoryService.getPortletContentResource(contentLink.getHref(), uriInfo);
         
         if (portletResource != null)
         {
            managedContentHandler.addManagedPortlet(category, managedContentResource, portletResource);
            return Response.status(Status.CREATED).header(HttpHeaders.CONTENT_LOCATION, uriInfo.getAbsolutePathBuilder().path(managedContentResource.getName()).build().toString()).build();
         }
         else
         {
            return Response.status(Status.BAD_REQUEST).build();
         }
      }
      else if (contentLink.getRel().equals(GadgetContentResource.LINK_REL))
      {
         throw new UnsupportedOperationException();
      }
      else if (contentLink.getRel().equals(WSRPContentResource.LINK_REL))
      {
         throw new UnsupportedOperationException();
      }
      else
      {
         return Response.status(Status.BAD_REQUEST).build();
      }
   }
   
   @DELETE
   @Path("/{categoryId}/managedcontent/{managedContentName}")
   public Response deleteManagedResource(@PathParam("categoryId") String categoryId, @PathParam("managedContentName") String managedContentName, @Context UriInfo uriInfo)
   {
      ManagedContentHandler managedContentHander = new ManagedContentHandler(uriInfo);
      CategoryHandler categoryHandler = new CategoryHandler(uriInfo);
      Category category = categoryHandler.getCategory(categoryId);
      
      if (managedContentHander.deleteManagedContent(category,managedContentName))
      {
         return Response.status(Status.OK).build();
      }
      else
      {
         return Response.status(Status.NOT_FOUND).build();
      }
   }
   
   @PUT
   @Consumes(MediaType.APPLICATION_XML)
   @Path("/{categoryId}/managedcontent/{managedContentName}")
   public void updateManagedResource(@PathParam("categoryId") String categoryId, @PathParam("managedContentName") String managedContentName, @Context UriInfo uriInfo, ManagedContentResource managedContentResource)
   {
      ManagedContentHandler managedContentHandler = new ManagedContentHandler(uriInfo);
      CategoryHandler categoryHandler = new CategoryHandler(uriInfo);
      Category category = categoryHandler.getCategory(categoryId);
      
      managedContentHandler.updateManagedContentResource(category, managedContentName, managedContentResource);
   }

}

