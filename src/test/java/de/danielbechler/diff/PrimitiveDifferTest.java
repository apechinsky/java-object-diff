/*
 * Copyright 2012 Daniel Bechler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.danielbechler.diff;

import de.danielbechler.diff.accessor.*;
import de.danielbechler.diff.node.*;
import de.danielbechler.diff.path.*;
import org.mockito.Mock;
import org.mockito.internal.debugging.*;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.*;

import static de.danielbechler.diff.Configuration.*;
import static de.danielbechler.diff.Configuration.PrimitiveDefaultValueMode.*;
import static de.danielbechler.diff.extension.MockitoExtensions.*;
import static de.danielbechler.diff.node.Node.State.*;
import static de.danielbechler.diff.node.NodeAssertions.*;
import static java.util.Arrays.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

/** @author Daniel Bechler */
public class PrimitiveDifferTest
{
	private PrimitiveDiffer differ;
	private Configuration configuration;
	@Mock private DifferDelegator differDelegator;
	@Mock private TypeAwareAccessor accessor;

	@BeforeMethod
	public void setUp()
	{
		initMocks(this);
		configuration = new Configuration();
		configuration.treatPrimitiveDefaultValuesAs(UNASSIGNED);
		differ = new PrimitiveDiffer(configuration);
	}

	@Test(dataProvider = "removals")
	public void testRemovedWhenPrimitiveDefaultModeIsUnassigned(final Class<?> type,
																final Object base,
																final Object working,
																final Object fresh) throws Exception
	{
		final Instances instances = whenInstancesAre(type, base, working, fresh);

		final Node node = differ.compare(Node.ROOT, instances);

		assertThat(node).self().hasState(REMOVED);

	}

	@AfterMethod
	public void tearDown()
	{
		new MockitoDebuggerImpl().printInvocations(differDelegator, accessor);
	}

	@Test(dataProvider = "changes")
	public void testChangedWhenPrimitiveDefaultModeIsUnassigned(final Class<?> type,
																final Object base,
																final Object working,
																final Object fresh) throws Exception
	{
		final Instances instances = whenInstancesAre(type, base, working, fresh);

		final Node node = differ.compare(Node.ROOT, instances);

		assertThat(node).self().hasState(CHANGED);
	}

	@Test(dataProvider = "additions")
	public void testAddedWhenPrimitiveDefaultModeIsUnassigned(final Class<?> type,
															  final Object base,
															  final Object working,
															  final Object fresh) throws Exception
	{
		final Instances instances = whenInstancesAre(type, base, working, fresh);

		final Node node = differ.compare(Node.ROOT, instances);

		assertThat(node).self().hasState(ADDED);
	}

	@Test(dataProvider = "changesWhenDefaultModeIsAssigned")
	public void testChangedWhenPrimitiveDefaultModeIsAssigned(final Class<?> type,
															  final Object base,
															  final Object working,
															  final Object fresh) throws Exception
	{
		configuration.treatPrimitiveDefaultValuesAs(PrimitiveDefaultValueMode.ASSIGNED);
		final Instances instances = whenInstancesAre(type, base, working, fresh);

		final Node node = differ.compare(Node.ROOT, instances);

		assertThat(node).self().hasState(CHANGED);
	}

	@Test(dataProvider = "wrapperTypes", expectedExceptions = IllegalArgumentException.class)
	public void testThrowsIllegalArgumentExceptionWhenNonPrimitiveTypeIsPassed(final Class<?> wrapperType) throws Exception
	{
		when(accessor.getType()).then(returnClass(wrapperType));

		differ.compare(Node.ROOT, Instances.of(accessor, "foo", "bar"));
	}

	@Test
	public void testIgnoresIgnorableNodes() throws Exception
	{
		final Instances instances = whenInstancesAre(int.class, 2, 1, 0);

		configuration.withoutProperty(PropertyPath.createBuilder()
												  .withRoot()
												  .withPropertyName("ignored")
												  .build());

		final Node node = new PrimitiveDiffer(configuration).compare(Node.ROOT, instances);

		assertThat(node).self().hasState(IGNORED);
	}

	private Instances whenInstancesAre(final Class<?> type,
										   final Object base,
										   final Object working,
										   final Object fresh)
	{
		when(accessor.getType()).then(returnClass(type));
		when(accessor.getPathElement()).thenReturn(new NamedPropertyElement("ignored"));
		return Instances.of(accessor, working, base, fresh);
	}

	private static <T> Object[] instances(final Class<T> type, final T base, final T working, final T fresh)
	{
		return new Object[] {type, base, working, fresh};
	}

	@DataProvider
	public Object[][] additions()
	{
		return new Object[][] {
				instances(int.class, 0, 1, 0),
				instances(long.class, 0L, 1L, 0L),
				instances(float.class, 0F, 1F, 0F),
				instances(double.class, 0D, 1D, 0D),
				instances(boolean.class, false, true, false),
		};
	}

	@DataProvider
	public Object[][] removals()
	{
		return new Object[][] {
				instances(int.class, 1, 0, 0),
				instances(long.class, 1L, 0L, 0L),
				instances(float.class, 1F, 0F, 0F),
				instances(double.class, 1D, 0D, 0D),
				instances(boolean.class, true, false, false),
		};
	}

	@DataProvider
	public Object[][] changes()
	{
		return new Object[][] {
				instances(int.class, 1, 2, 0),
				instances(long.class, 1L, 2L, 0L),
				instances(float.class, 1F, 2F, 0F),
				instances(double.class, 1D, 2D, 0D),
		};
	}

	@DataProvider
	public Object[][] wrapperTypes()
	{
		return new Object[][] {
				new Object[] {Integer.class},
				new Object[] {Long.class},
				new Object[] {Float.class},
				new Object[] {Double.class},
				new Object[] {Short.class},
				new Object[] {Byte.class},
				new Object[] {Boolean.class},
		};
	}

	@DataProvider
	public Object[][] changesWhenDefaultModeIsAssigned()
	{
		final List<Object[]> objects = new ArrayList<Object[]>();
		objects.addAll(asList(additions()));
		objects.addAll(asList(changes()));
		objects.addAll(asList(removals()));
		//noinspection ToArrayCallWithZeroLengthArrayArgument
		return objects.toArray(new Object[0][0]);
	}
}
