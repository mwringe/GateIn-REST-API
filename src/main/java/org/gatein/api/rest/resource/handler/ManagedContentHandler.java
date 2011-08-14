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

import java.util.Iterator;

import javax.ws.rs.core.UriInfo;

import org.gatein.api.content.Category;
import org.gatein.api.content.Content;
import org.gatein.api.content.Gadget;
import org.gatein.api.content.ManagedContent;
import org.gatein.api.content.Portlet;
import org.gatein.api.content.WSRP;
import org.gatein.api.id.Id;
import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.content.GadgetContentResource;
import org.gatein.api.rest.resource.content.ManagedContentResource;
import org.gatein.api.rest.resource.content.ManagedContentsResource;
import org.gatein.api.rest.resource.content.PortletContentResource;
import org.gatein.api.rest.service.CategoriesService;
import org.gatein.api.rest.service.GadgetRepositoryService;
import org.gatein.api.rest.service.PortletRespositoryService;
import org.gatein.api.util.IterableCollection;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class ManagedContentHandler extends Handler
{
   protected UriInfo uriInfo;
   
   public ManagedContentHandler(UriInfo uriInfo)
   {
      this.uriInfo = uriInfo;
   }
   
   public ManagedContentsResource getManagedContentsResource(Category category)
   {
      ManagedContentsResource managedContentsResource = new ManagedContentsResource();
      
      IterableCollection<String> managedContents = category.getKnownManagedContentNames();
      Iterator<String> iterator = managedContents.iterator();
      while (iterator.hasNext())
      {
         String managedContentName = iterator.next();
         ManagedContent managedContent = category.getManagedContent(managedContentName);
         
         managedContentsResource.getManagedContents().add(toManagedContentResource(managedContent, category));
      }
      
      String selfHref = uriInfo.getBaseUriBuilder().clone().path(CategoriesService.SERVICE_PATH).path(category.getName()).path("managedcontent").build().toString();
      Link selfLink = new Link("self", selfHref);
      managedContentsResource.setSelfLink(selfLink);
      
      return managedContentsResource;
   }
   
   public ManagedContentResource toManagedContentResource(ManagedContent managedContent, Category category)
   {
      ManagedContentResource managedContentResource = new ManagedContentResource();
      
      managedContentResource.setName(managedContent.getName());
      managedContentResource.setDisplayName(managedContent.getDisplayName());
      managedContentResource.setDescription(managedContent.getDescription());
      managedContentResource.setId(managedContent.getId().toString());
      
      
      Content content = managedContent.getContent();
      if (content instanceof Portlet)
      {
         Portlet portlet = (Portlet)content;
         
         String relation = PortletContentResource.LINK_REL;
         String href = this.uriInfo.getBaseUriBuilder().clone().path(PortletRespositoryService.SERVICE_PATH).path(portlet.getApplicationName()).path(portlet.getPortletName()).build().toString();
         Link link = new Link(relation, href);
         
         managedContentResource.setContentLink(link);
      }
      else if (content instanceof WSRP)
      {
         WSRP wsrp = (WSRP)content;
      }
      else if (content instanceof Gadget)
      {
         Gadget gadget = (Gadget)content;
         
         String relation = GadgetContentResource.LINK_REL;
         String href = this.uriInfo.getBaseUriBuilder().clone().path(GadgetRepositoryService.SERVICE_PATH).path(gadget.getName()).build().toString();
         
         Link link = new Link(relation, href);
         
         managedContentResource.setContentLink(link);
      }
      
      String href = this.uriInfo.getBaseUriBuilder().clone().path(CategoriesService.SERVICE_PATH).path(category.getName()).path("managedcontent").path(managedContent.getName()).build().toString();
      Link selfLink = new Link("self", href);
      managedContentResource.setSelfLink(selfLink);
      
      return managedContentResource;
   }
   
   public void updateManagedContentResource(Category category, String originalName, ManagedContentResource managedContentResource)
   {
      ManagedContent managedContent = category.getManagedContent(originalName);
      
      //check if the non changeable values are changed or not, if they are changed, then throw an error.
      if (managedContent.getId().toString().equals(managedContentResource.getId()))
      {
         System.out.println("IDs match");
      }
      else
      {
         System.out.println("IDs don't match");
      }
      
      managedContent.setDescription(managedContentResource.getDescription());
      managedContent.setDisplayName(managedContentResource.getDisplayName());      
   }
   
   public ManagedContentResource getManagedContentResource(Category category, String managedContentName)
   {
      ManagedContent managedContent = category.getManagedContent(managedContentName);
      if (managedContent == null)
      {
         return null;
      }
      else
      {
         return toManagedContentResource(managedContent, category);
      }
   }
   
   public boolean deleteManagedContent(Category category, String managedContentName)
   {
      if (category.contains(managedContentName))
      {
         category.removeContent(managedContentName);
         return true;
      }
      else
      {
         return false;
      }
   }

   public void addManagedPortlet(Category category, ManagedContentResource managedContentResource, PortletContentResource portletResource)
   {  
      Id<Portlet> portletId = gateIn.portletId(portletResource.getApplicationName(), portletResource.getPortletName());
      category.addContent(portletId, managedContentResource.getName());
   }

}

