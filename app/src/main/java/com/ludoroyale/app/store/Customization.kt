package com.ludoroyale.app.store
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
data class CosmeticProfile(val playerId:String,val owned:Set<String>,val equipped:Map<StoreCategory,String>)
class CosmeticSyncRepository(private val db:FirebaseFirestore=FirebaseFirestore.getInstance()){suspend fun save(profile:CosmeticProfile)=runCatching{db.collection("players").document(profile.playerId).set(mapOf("cosmetics_owned" to profile.owned.toList(),"cosmetics_equipped" to profile.equipped.mapKeys{it.key.name}),com.google.firebase.firestore.SetOptions.merge()).await()}}
