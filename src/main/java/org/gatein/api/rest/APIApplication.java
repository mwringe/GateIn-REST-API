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

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.gatein.api.rest.service.APIService;
import org.gatein.api.rest.service.CategoriesService;
import org.gatein.api.rest.service.ContentRegistryService;
import org.gatein.api.rest.service.ContentsService;
import org.gatein.api.rest.service.DocumentationService;
import org.gatein.api.rest.service.GadgetRepositoryService;
import org.gatein.api.rest.service.PagesService;
import org.gatein.api.rest.service.PortletRespositoryService;
import org.gatein.api.rest.service.SitesService;
import org.gatein.api.rest.service.WSRPRepositoryService;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class APIApplication extends Application
{

   private Set<Object> singletons;

   public APIApplication()
   {
      singletons = new HashSet<Object>();
      singletons.add(new APIService());
      singletons.add(new DocumentationService());
      singletons.add(new SitesService());
      singletons.add(new ContentRegistryService());
      singletons.add(new PagesService());
      singletons.add(new CategoriesService());
      singletons.add(new ContentsService());
      singletons.add(new GadgetRepositoryService());
      singletons.add(new PortletRespositoryService());
      singletons.add(new WSRPRepositoryService());
   }

   public Set<Class<?>> getClasses()
   {
      return null;
   }

   public Set<Object> getSingletons()
   {
      return singletons;
   }
}
