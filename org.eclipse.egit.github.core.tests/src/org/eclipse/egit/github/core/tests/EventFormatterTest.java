/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Jason Tsay (GitHub Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.egit.github.core.tests;

import org.eclipse.egit.github.core.client.EventFormatter;
import org.eclipse.egit.github.core.client.GsonUtils;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.FollowPayload;
import org.eclipse.egit.github.core.event.PullRequestPayload;
import org.junit.Test;

import static org.eclipse.egit.github.core.event.Event.TYPE_FOLLOW;
import static org.eclipse.egit.github.core.event.Event.TYPE_PULL_REQUEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Unit tests of {@link EventFormatter}
 */
public class EventFormatterTest {

	/**
	 * Follow event payload returned as {@link FollowPayload}
	 */
	@Test
	public void followPayload() {
		Event event = GsonUtils.fromJson("{\"type\":\"" + TYPE_FOLLOW
				+ "\",\"payload\":{}}", Event.class);
		assertNotNull(event);
		assertNotNull(event.getPayload());
		assertEquals(FollowPayload.class, event.getPayload().getClass());
	}

	/**
	 * Unknown event payload returned as {@link EventPayload}
	 */
	@Test
	public void unknownPayload() {
		Event event = GsonUtils.fromJson(
				"{\"type\":\"NotAnEventType\",\"payload\":{}}", Event.class);
		assertNotNull(event);
		assertNotNull(event.getPayload());
		assertEquals(EventPayload.class, event.getPayload().getClass());
	}

	/**
	 * Event with missing type has payload returned as {@link EventPayload}
	 */
	@Test
	public void missingType() {
		Event event = GsonUtils.fromJson("{\"payload\":{}}", Event.class);
		assertNotNull(event);
		assertNotNull(event.getPayload());
		assertEquals(EventPayload.class, event.getPayload().getClass());
	}

	/**
	 * Missing payload
	 */
	@Test
	public void missingPayload() {
		Event event = GsonUtils.fromJson("{}", Event.class);
		assertNotNull(event);
		assertNull(event.getPayload());
	}

	private static final String labeledActionPullRequestEventText = "{\"type\":\"" + TYPE_PULL_REQUEST
			+ "\",\"payload\":{\n" +
			"  \"action\": \"labeled\",\n" +
			"  \"number\": 1,\n" +
			"  \"pull_request\": { },\n" +
			"  \"label\": {\n" +
			"    \"id\": 518329077,\n" +
			"    \"url\": \"url_enhancement\",\n" +
			"    \"name\": \"enhancement\",\n" +
			"    \"color\": \"84b6eb\",\n" +
			"    \"default\": true\n" +
			"  }" +
			"}}";

	@Test
	public void pullRequestPayload() {
		Event event = GsonUtils.fromJson(labeledActionPullRequestEventText, Event.class);
		assertNotNull(event);
		assertNotNull(event.getPayload());
		assertEquals(PullRequestPayload.class, event.getPayload().getClass());
	}

	@Test
	public void labeledPullRequestPayload() {
		Event event = GsonUtils.fromJson(labeledActionPullRequestEventText, Event.class);
		PullRequestPayload pullRequestPayload = (PullRequestPayload)event.getPayload();
		assertEquals("labeled", pullRequestPayload.getAction());
		assertNotNull(pullRequestPayload.getLabel());
		assertEquals("enhancement", pullRequestPayload.getLabel().getName());
		assertEquals("84b6eb", pullRequestPayload.getLabel().getColor());
		assertEquals("url_enhancement", pullRequestPayload.getLabel().getUrl());
	}
}
