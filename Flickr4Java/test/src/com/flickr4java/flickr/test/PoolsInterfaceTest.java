/* Copyright 2004, Aetrion LLC.  All Rights Reserved. */

package com.flickr4java.flickr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.pools.PoolsInterface;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.util.IOUtilities;

import org.junit.Before;
import org.junit.Test;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.oauth.OAuthService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

/**
 * @author Anthony Eden
 */
public class PoolsInterfaceTest {

    Flickr flickr = null;
    Properties properties = null;

    @Before
    public void setUp() throws ParserConfigurationException, IOException, FlickrException, SAXException {
        Flickr.debugStream = true;
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);

			OAuthService service = new ServiceBuilder().provider(FlickrApi.class)
					.apiKey(properties.getProperty("apiKey")).apiSecret(properties.getProperty("secret")).build();
			REST rest = new REST();
			rest.setHost(properties.getProperty("host"));

            flickr = new Flickr(properties.getProperty("apiKey"), rest);

			Auth auth = new Auth();
			auth.setPermission(Permission.READ);
			auth.setToken(properties.getProperty("token"));
			auth.setTokenSecret(properties.getProperty("tokensecret"));

			RequestContext requestContext = RequestContext.getRequestContext();
			requestContext.setAuth(auth);
			flickr.setAuth(auth);
        } finally {
            IOUtilities.close(in);
        }
    }

    @Test
    public void testAddAndRemove() throws FlickrException, IOException, SAXException {
        String photoId = properties.getProperty("photoid");
        String groupId = properties.getProperty("testgroupid");
        PoolsInterface iface = flickr.getPoolsInterface();
        iface.add(photoId, groupId);
        iface.remove(photoId, groupId);
    }

    @Test
    public void testGetContext() {

    }

    @Test
    public void testGetGroups() throws FlickrException, IOException, SAXException {
        PoolsInterface iface = flickr.getPoolsInterface();
        Collection<Group> groups = iface.getGroups();
        assertNotNull(groups);
        assertEquals(248, groups.size());
    }

    @Test
    public void testGetPhotos() throws FlickrException, IOException, SAXException {
        String groupId = properties.getProperty("testgroupid");
        PoolsInterface iface = flickr.getPoolsInterface();
        Collection<Photo> photos = iface.getPhotos(groupId, null, 0, 0);
        assertNotNull(photos);
        assertEquals(0, photos.size());
    }

}
