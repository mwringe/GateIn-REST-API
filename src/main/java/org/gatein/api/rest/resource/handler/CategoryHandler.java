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
package org.gatein.api.rest.resource.handler;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import org.gatein.api.content.Category;
import org.gatein.api.content.ContentRegistry;
import org.gatein.api.portal.Portal;
import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.content.CategoryResource;
import org.gatein.api.rest.resource.content.ManagedContentsResource;
import org.gatein.api.rest.service.CategoriesService;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class CategoryHandler extends Handler
{

   protected UriInfo uriInfo;
   
   public CategoryHandler(UriInfo uriInfo)
   {
      this.uriInfo = uriInfo;
   }
   
   public CategoryResource toCategoryResource(Category category)
   {
      CategoryResource categoryResource = new CategoryResource();

      categoryResource.setName(category.getName());
      categoryResource.setDisplayName(category.getDisplayName());
      categoryResource.setDescription(category.getDescription());
      categoryResource.setId(category.getId().toString());

      String href = uriInfo.getBaseUriBuilder().clone().path(CategoriesService.SERVICE_PATH).path(categoryResource.getId()).path("managedcontent").build().toString();
      categoryResource.setManagedContentsLink(new Link(ManagedContentsResource.LINK_REL, href));

      String selfHref = uriInfo.getBaseUriBuilder().clone().path(CategoriesService.SERVICE_PATH).path(categoryResource.getId()).build().toString();
      categoryResource.setSelfLink(new Link("self", selfHref));


      return categoryResource;
   }
   
   public CategoryResource getCategoryResource(String categoryId)
   {
      Category category = getCategory(categoryId);
      
      if (category == null)
      {
         return null;
      }
      
      return toCategoryResource(category);
   }
   
   public Category getCategory(String categoryId)
   {
      //TODO: fix this, content is based on the portal object, but there is really only one contentregistry for all portals
      Portal portal = gateIn.getDefaultPortal();
      ContentRegistry contentRegistry = portal.getContentRegistry();
      
      Category category = contentRegistry.getCategory(categoryId);
      
      return category;
   }
   
   public boolean deleteCategory(String categoryId)
   {
      //TODO: fix this, content is based on the portal object, but there is really only one contentregistry for all portals
      Portal portal = gateIn.getDefaultPortal();
      ContentRegistry contentRegistry = portal.getContentRegistry();
      
      if (contentRegistry.getCategory(categoryId) != null)
      {
         contentRegistry.deleteCategory(categoryId);
         return true;
      }
      else
      {
         return false;
      }
   }

   public URI createCategory(CategoryResource categoryResource)
   {
      //TODO: fix this, content is based on the portal object, but there is really only one contentregistry for all portals
      Portal portal = gateIn.getDefaultPortal();
      ContentRegistry contentRegistry = portal.getContentRegistry();
      
      Category category = contentRegistry.getOrCreateCategory(categoryResource.getName());
      category.setDescription(categoryResource.getDescription());
      //category.setDisplayName(categoryResource.getDisplayName());
      
      return uriInfo.getBaseUriBuilder().clone().path(CategoriesService.SERVICE_PATH).path(category.getName()).build();
   }

   public boolean updateCategory(CategoryResource categoryResource, Category category)
   {
      //check if its valid to update the category or not
      if (!category.getName().equals(category.getName()))
      {
         return false;
      }
      else if (!category.getId().toString().equals(categoryResource.getId()))
      {
         return false;
      } 
      else if (categoryResource.getManagedContentsLink() == null || !categoryResource.getManagedContentsLink().getHref().equals(uriInfo.getBaseUriBuilder().clone().path(CategoriesService.SERVICE_PATH).path(categoryResource.getName()).path("managedcontent").build().toString()))
      {
         return false;
      }
      
      category.setDescription(categoryResource.getDescription());
      category.setDisplayName(categoryResource.getDisplayName());
      return true;
   }
   
}

