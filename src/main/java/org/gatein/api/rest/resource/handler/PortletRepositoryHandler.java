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

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.gatein.api.content.Content;
import org.gatein.api.content.Portlet;
import org.gatein.api.portal.Portal;
import org.gatein.api.rest.resource.content.ApplicationResource;
import org.gatein.api.rest.resource.content.PortletContentResource;
import org.gatein.api.rest.resource.content.PortletRepositoryResource;
import org.gatein.api.util.IterableIdentifiableCollection;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class PortletRepositoryHandler extends Handler
{

   protected UriInfo uriInfo;
   
   public PortletRepositoryHandler(UriInfo uriInfo)
   {
     this.uriInfo = uriInfo;
   }
   
   public PortletRepositoryResource getApplications()
   {
      PortletRepositoryResource resource = new PortletRepositoryResource();

      PortletHandler portletHandler = new PortletHandler(uriInfo);
      
      Portal portal = gateIn.getDefaultPortal();
      
      Map<String, ApplicationResource> applications = new HashMap<String, ApplicationResource>();

      IterableIdentifiableCollection<Content> contents = portal.getContentRegistry().getAll();

      while (contents.iterator().hasNext())
      {
         Content content = null;
         content = contents.iterator().next();

         if (content != null && content.getType().getName().equals("portlet"))
         {
            if (content instanceof Portlet)
            {
               Portlet portlet = (Portlet)content;
               
               ApplicationResource applicationResource;
               if (applications.containsKey(portlet.getApplicationName()))
               {
                  applicationResource = applications.get(portlet.getApplicationName());
               }
               else
               {
                  applicationResource = new ApplicationResource();
                  applicationResource.setName(portlet.getApplicationName());
                  applications.put(portlet.getApplicationName(), applicationResource);
               }
               
               applicationResource.getPortlets().add(toPortletContentResource(portlet));
               
               applications.put(portlet.getApplicationName(), applicationResource);
            }
         }
      }
      
      resource.getApplications().addAll(applications.values());
      
      return resource;
   }
   
   public PortletContentResource getPortlet(String applicationId, String portletId)
   {
      PortletHandler portletHandler = new PortletHandler(uriInfo);
      
      Portal portal = gateIn.getDefaultPortal();
      
      try
      {
         Portlet portlet = portal.getContentRegistry().get(gateIn.portletId(applicationId, portletId));
         return this.toPortletContentResource(portlet);
      }
      catch (Exception e)
      {
         //TODO: find a better solution than this, but we don't want to rely on PC just for an exception name :(
         if (!e.getCause().getClass().toString().equals("class org.gatein.pc.api.NoSuchPortletException"))
         {
            throw new WebApplicationException(e);
         }
         else
         {
            return null;
         }
      }
   }
   
   public ApplicationResource getApplication(String applicationId)
   {
      PortletHandler portletHandler = new PortletHandler(uriInfo);
      Portal portal = gateIn.getDefaultPortal();
      
      IterableIdentifiableCollection<Content> contents = portal.getContentRegistry().getAll();

      ApplicationResource applicationResource = new ApplicationResource();
      applicationResource.setName(applicationId);
      
      while (contents.iterator().hasNext())
      {
         Content content = null;
         content = contents.iterator().next();

         if (content != null && content.getType().getName().equals("portlet"))
         {
            if (content instanceof Portlet && ((Portlet)content).getApplicationName().equals(applicationId))
            {
               Portlet portlet = (Portlet)content;
               applicationResource.getPortlets().add(toPortletContentResource(portlet));
               //resource.get
               //resource.getPortlets().add(portletHandler.toPortletResource((Portlet) content));
            }
         }
      }

      if (applicationResource.getPortlets().isEmpty())
      {
         return null;
      }
      else
      {
         return applicationResource;  
      }
   }
   
   public PortletContentResource toPortletContentResource(Portlet portlet)
   {
      PortletContentResource portletResource = new PortletContentResource();
      
      portletResource.setApplicationName(portlet.getApplicationName());
      portletResource.setPortletName(portlet.getPortletName());
      
      portletResource.setDisplayName(portlet.getDisplayName());
      portletResource.setName(portlet.getName());
      portletResource.setId(portlet.getId().toString());
      
      return portletResource;
   }

}

