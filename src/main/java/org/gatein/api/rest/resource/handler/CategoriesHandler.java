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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.gatein.api.content.Category;
import org.gatein.api.content.ContentRegistry;
import org.gatein.api.portal.Portal;
import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.content.CategoriesResource;
import org.gatein.api.rest.resource.content.CategoryResource;
import org.gatein.api.rest.service.CategoriesService;
import org.gatein.api.util.IterableIdentifiableCollection;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class CategoriesHandler extends Handler
{
   
   public CategoriesHandler(UriInfo uriInfo)
   {
      this.uriInfo = uriInfo;
   }
   
   public CategoriesResource getCategoriesResource()
   {
      CategoriesResource categoriesResource = new CategoriesResource();
      
      //add the navigation links
      UriBuilder builder = uriInfo.getBaseUriBuilder().clone().path(CategoriesService.SERVICE_PATH);
      categoriesResource.setSelfLink(new Link("self",builder.build().toString()));
      
      //add the categories
      categoriesResource.getCategories().addAll(getCategoryResources());
      
      return categoriesResource;
   }
   
//   public List<CategoryResource> getCategoryResources()
//   {  
//      List<CategoryResource> categoryResources = new ArrayList<CategoryResource>();
//      
//      IterableIdentifiableCollection<Portal> portals = gateIn.getPortals();
//      Iterator<Portal> portalIterator = portals.iterator();
//      while (portalIterator.hasNext())
//      {
//         Portal portal = portalIterator.next();
//         categoryResources.addAll(getCategoriesForPortal(portal.getName()));
//      }
//
//      return categoryResources;
//   }
   
   public List<CategoryResource> getCategoryResources()
   {  
      List<CategoryResource> categoryResources = new ArrayList<CategoryResource>();
      
      //TODO: fix this, content is based on the portal object, but there is really only one contentregistry for all portals
      Portal portal = gateIn.getDefaultPortal();
      
      ContentRegistry contentRegistry = portal.getContentRegistry();
      
      IterableIdentifiableCollection<Category> categories;
      categories = contentRegistry.getAllCategories();
      
      if (categories != null)
      {
         CategoryHandler categoryHandler = new CategoryHandler(uriInfo);
         Iterator<Category> iterator = categories.iterator();
         while (iterator.hasNext())
         {
            CategoryResource categoryResource = categoryHandler.toCategoryResource(iterator.next());
            categoryResources.add(categoryResource);
         }
      }
      
      return categoryResources;
   }
}

