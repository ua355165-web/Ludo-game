package com.ludoroyale.app.rewards
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
class RewardsTest{@Test fun cycleHasSevenDays(){assertEquals(7,DailyRewards.cycle.size)};@Test fun consecutiveClaimAdvancesStreak(){val s=DailyState(1,"2026-10-01",1,1);assertFalse(s.claimedToday)};@Test fun eventExpires(){assertTrue(Events.sample.active)};@Test fun missionNeedsTarget(){assertFalse(MissionCatalog.definitions.first{it.id=="games3"}.copy(progress=2).completed);assertTrue(MissionCatalog.definitions.first{it.id=="games3"}.copy(progress=3).completed)}}
