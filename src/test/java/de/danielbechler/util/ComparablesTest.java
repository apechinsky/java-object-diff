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

package de.danielbechler.util;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.fest.assertions.api.Assertions.assertThat;

import de.danielbechler.diff.integration.Contact;
import de.danielbechler.diff.integration.PhoneBook;
import de.danielbechler.diff.integration.PhoneNumber;
import de.danielbechler.diff.mock.ObjectWithExceptionThrowingDefaultConstructor;
import de.danielbechler.diff.mock.ObjectWithPrivateDefaultConstructor;
import de.danielbechler.diff.mock.ObjectWithString;
import de.danielbechler.diff.mock.ObjectWithoutDefaultConstructor;
import de.danielbechler.diff.node.Node;

/**
 * @author Anton Pechinsky
 */
public class ComparablesTest
{

    @Test
    public void testIsEqualByComparisonEqual() throws Exception {
        assertThat(Comparables.isEqualByComparison("string", "string")).isTrue();
    }

    @Test
    public void testIsEqualByComparisonNotEqual() throws Exception {
        assertThat(Comparables.isEqualByComparison("string1", "string2")).isFalse();
    }

    @Test
	public void testIsEqualByComparisonWithNullUnsafeComparable() throws Exception
    {
        assertThat(Comparables.isEqualByComparison(BigDecimal.ONE, null)).isFalse();
	}

	@Test
	public void testIsEqualByComparisonWithNullUnsafeComparableSwapArguments() throws Exception
    {
        assertThat(Comparables.isEqualByComparison(null, BigDecimal.ONE)).isFalse();
	}

}
