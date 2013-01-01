/*
 * Copyright (c) 2012 Tom Parker <thpr@users.sourceforge.net>
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
package tokencontent;

import org.junit.Test;

import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.SkillCost;
import pcgen.cdom.enumeration.Type;
import pcgen.cdom.facet.FacetLibrary;
import pcgen.cdom.facet.analysis.ListSkillCostFacet;
import pcgen.cdom.facet.input.RaceInputFacet;
import pcgen.cdom.list.ClassSkillList;
import pcgen.core.PCClass;
import pcgen.core.Race;
import pcgen.core.Skill;
import pcgen.gui2.facade.MockUIDelegate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.token.CDOMToken;
import pcgen.rules.persistence.token.ParseResult;
import pcgen.util.chooser.ChooserFactory;
import plugin.lsttokens.choose.SkillToken;
import plugin.lsttokens.race.MonccskillToken;
import plugin.lsttokens.testsupport.TokenRegistration;
import tokenmodel.testsupport.AbstractTokenModelTest;

public class RaceMonCCSkillTest extends AbstractTokenModelTest
{

	private static MonccskillToken token = new MonccskillToken();
	private static SkillToken CHOOSE_SKILL_TOKEN = new SkillToken();
	protected RaceInputFacet raceInputFacet = FacetLibrary
			.getFacet(RaceInputFacet.class);

	private ListSkillCostFacet lscFacet;
	private Skill sk;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		lscFacet = FacetLibrary.getFacet(ListSkillCostFacet.class);
		sk = context.ref.constructCDOMObject(Skill.class, "MySkill");
		PCClass dragon =
				context.ref.constructCDOMObject(PCClass.class, "Dragon");
		dragon.addToListFor(ListKey.TYPE, Type.MONSTER);
		TokenRegistration.register(CHOOSE_SKILL_TOKEN);
		ChooserFactory.setDelegate(new MockUIDelegate());
	}

	@Test
	public void testDirect() throws PersistenceLayerException
	{
		Race source = create(Race.class, "Source");
		ParseResult result = token.parseToken(context, source, "MySkill");
		if (result != ParseResult.SUCCESS)
		{
			result.printMessages();
			fail("Test Setup Failed");
		}
		finishLoad();
		raceFacet.set(id, getSelectionObject(source));
		ClassSkillList dragonCSL =
				context.ref.silentlyGetConstructedCDOMObject(
					ClassSkillList.class, "Dragon");
		assertTrue(lscFacet.contains(id, dragonCSL, SkillCost.CROSS_CLASS, sk));
		raceFacet.remove(id);
		assertFalse(lscFacet.contains(id, dragonCSL, SkillCost.CROSS_CLASS, sk));
	}

	@Override
	public CDOMToken<?> getToken()
	{
		return token;
	}
}