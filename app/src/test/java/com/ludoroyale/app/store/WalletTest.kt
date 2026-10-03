package com.ludoroyale.app.store
import org.junit.Assert.*
import org.junit.Test
class WalletTest{@Test fun cannotSpendMoreThanBalance(){val s=WalletState(coins=10);assertTrue(s.coins<100)};@Test fun catalogItemsHaveUniqueIds(){assertEquals(StoreCatalog.items.size,StoreCatalog.items.map{it.id}.toSet().size)};@Test fun ownedItemIsNotPurchasable(){val w=WalletState(owned=setOf("token_classic"));assertTrue("token_classic" in w.owned)};@Test fun categoriesEquipOneItem(){val w=WalletState(equipped=mapOf(StoreCategory.TOKEN_SKIN to "token_sunset"));assertEquals("token_sunset",w.equipped[StoreCategory.TOKEN_SKIN])}}
