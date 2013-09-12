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

/**
 * @author Daniel Bechler
 * @author Anton Pechinsky
 */
public class Comparables
{
	private Comparables()
	{
	}

    /**
     * Performs null-safe equality check for arguments implementing {@link Comparable}
     */
	public static <T extends Comparable<T>> boolean isEqualByComparison(final T a, final T b)
	{
        if (a == b) {
            return true;
        }
        else if (a == null || b == null) {
            return false;
        }
        else {
            return a.compareTo(b) == 0;
        }
	}
}
