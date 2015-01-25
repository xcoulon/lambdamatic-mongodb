/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package org.lambdamatic.mongodb;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.lambdamatic.mongodb.annotations.Document;
import org.lambdamatic.mongodb.testutils.DropMongoCollectionsRule;
import org.lambdamatic.mongodb.types.geospatial.Location;
import org.lambdamatic.mongodb.types.geospatial.Polygon;

import com.mongodb.MongoClient;
import com.sample.Foo;
import com.sample.Foo.FooBuilder;
import com.sample.FooCollection;

/**
 * Testing the MongoDB Lambda-based Fluent API
 * 
 * @author Xavier Coulon <xcoulon@redhat.com>
 *
 */
public class MongoGeolocationQueryTest {

	private static final String DATABASE_NAME = "lambdamatic-tests";

	private static final String COLLECTION_NAME = ((Document) Foo.class.getAnnotation(Document.class)).collection();

	private MongoClient mongoClient = new MongoClient();

	@Rule
	public DropMongoCollectionsRule collectionCleaning = new DropMongoCollectionsRule(mongoClient, DATABASE_NAME,
			COLLECTION_NAME);

	private FooCollection fooCollection;

	@Before
	public void setup() throws UnknownHostException {
		this.fooCollection = new FooCollection(mongoClient, DATABASE_NAME, COLLECTION_NAME);
		// insert test data
		final Foo foo1 = new FooBuilder().withStringField("Item1").withLocation(40.72, -73.92).build();
		final Foo foo2 = new FooBuilder().withStringField("Item2").withLocation(40.73, -73.92).build();
		final Foo foo3 = new FooBuilder().withStringField("Item3").withLocation(40.73, -73.92).build();
		final Foo foo4 = new FooBuilder().withStringField("Item4").withLocation(40.72, -73.92).build();
		final Foo foo5 = new FooBuilder().withStringField("Item5").withLocation(40.0, -73.0).build();
		this.fooCollection.insert(foo1, foo2, foo3, foo4, foo5);
		// mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME).createIndex("location",
		// new CreateIndexOptions().twoDSphereIndexVersion(2));
	}

	@Test
	public void shouldFindGeoWithinPolygon() throws IOException {
		// when
		final Polygon corners = new Polygon(new Location(40.70, -73.90), new Location(40.75, -73.90), new Location(
				40.75, -73.95), new Location(40.70, -73.95));
		final List<Foo> matches = fooCollection.find(f -> f.location.geoWithin(corners)).toList();
		// then
		assertThat(matches).isNotNull().hasSize(4).are(new Condition<Foo>("Checking location is set") {
			@Override
			public boolean matches(final Foo item) {
				return item.getLocation() != null && item.getLocation().getLatitude() != 0
						&& item.getLocation().getLongitude() != 0;
			}
		});
	}

	@Test
	public void shouldFindGeoWithinLocations() throws IOException {
		// when
		final Location[] corners = new Location[] { new Location(40.70, -73.90), new Location(40.75, -73.90),
				new Location(40.75, -73.95), new Location(40.70, -73.95) };
		final List<Foo> matches = fooCollection.find(f -> f.location.geoWithin(corners)).toList();
		// then
		assertThat(matches).isNotNull().hasSize(4);
	}

}