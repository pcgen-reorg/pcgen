/*
 * Copyright (c) 2007 Tom Parker <thpr@users.sourceforge.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package plugin.lsttokens.kit.gear;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import pcgen.cdom.enumeration.StringKey;
import pcgen.core.SizeAdjustment;
import pcgen.core.kit.KitGear;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMSubLineLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractKitTokenTestCase;

public class SizeTokenTest extends AbstractKitTokenTestCase<KitGear>
{

	static SizeToken token = new SizeToken();
	static CDOMSubLineLoader<KitGear> loader = new CDOMSubLineLoader<KitGear>(
			"TABLE", KitGear.class);

	@Override
	public Class<KitGear> getCDOMClass()
	{
		return KitGear.class;
	}

	@Override
	public CDOMSubLineLoader<KitGear> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<KitGear> getToken()
	{
		return token;
	}

	@Override
	@Before
	public void setUp() throws PersistenceLayerException, URISyntaxException
	{
		super.setUp();
		SizeAdjustment ps = primaryContext.getReferenceContext().constructCDOMObject(
				SizeAdjustment.class, "Small");
		ps.put(StringKey.ABB, "S");
		primaryContext.getReferenceContext().registerAbbreviation(ps, "S");
		SizeAdjustment pm = primaryContext.getReferenceContext().constructCDOMObject(
				SizeAdjustment.class, "Medium");
		pm.put(StringKey.ABB, "M");
		primaryContext.getReferenceContext().registerAbbreviation(pm, "M");
		SizeAdjustment ss = secondaryContext.getReferenceContext().constructCDOMObject(
				SizeAdjustment.class, "Small");
		ss.put(StringKey.ABB, "S");
		secondaryContext.getReferenceContext().registerAbbreviation(ss, "S");
		SizeAdjustment sm = secondaryContext.getReferenceContext().constructCDOMObject(
				SizeAdjustment.class, "Medium");
		sm.put(StringKey.ABB, "M");
		secondaryContext.getReferenceContext().registerAbbreviation(sm, "M");
	}

	@Test
	public void testInvalidNotASize()
	{
		assertFalse(token.parseToken(primaryContext, primaryProf, "W").passed());
	}

	@Test
	public void testRoundRobinS() throws PersistenceLayerException
	{
		runRoundRobin("S");
	}

	@Test
	public void testRoundRobinPC() throws PersistenceLayerException
	{
		//Special Case
		runRoundRobin("PC");
	}

	@Test
	public void testRoundRobinM() throws PersistenceLayerException
	{
		runRoundRobin("M");
	}
}
