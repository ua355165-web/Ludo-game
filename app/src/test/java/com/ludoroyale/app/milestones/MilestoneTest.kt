package com.ludoroyale.app.milestones
import com.ludoroyale.app.progression.PlayerProgress
import org.junit.Assert.*
import org.junit.Test
class MilestoneTest{@Test fun progressUsesRealStats(){val m=MilestoneCatalog.all(PlayerProgress(games=10,wins=1)).first{it.id=="play10"};assertEquals(10,m.progress);assertTrue(m.completed)};@Test fun incompleteCannotComplete(){assertFalse(MilestoneCatalog.all(PlayerProgress()).first{it.id=="win10"}.completed)};@Test fun categoriesExist(){assertTrue(MilestoneCategory.entries.size>=8)}}
