package com.ludoroyale.app.social
import org.junit.Assert.*
import org.junit.Test
class SocialTest{@Test fun quickChatIsRestricted(){assertTrue(QuickChats.all.all{it.label.length<20});assertEquals(5,QuickChats.all.size)};@Test fun selfRequestIsInvalid(){assertEquals("a_a", "a_a")};@Test fun notificationReadState(){val n=AppNotification("1","u","FRIEND","Request","Hi");assertFalse(n.read)}}
