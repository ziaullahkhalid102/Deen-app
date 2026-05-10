package com.deenapp.data.service

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleDriveService @Inject constructor() {

    companion object {
        private const val APP_FOLDER = "DeenApp"
        private const val PROFILE_FILE = "profile.json"
        private const val POSTS_FILE = "posts.json"
        private const val CHATS_FILE = "chats.json"
        private const val SETTINGS_FILE = "settings.json"
    }

    private var driveService: Drive? = null
    private var appFolderId: String? = null

    suspend fun initialize(context: Context, accountEmail: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val account = GoogleSignIn.getLastSignedInAccount(context)
                if (account != null) {
                    val credential = GoogleAccountCredential.usingOAuth2(
                        context, listOf(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_APPDATA)
                    )
                    credential.selectedAccount = account.account
                    driveService = Drive.Builder(
                        NetHttpTransport(),
                        GsonFactory.getDefaultInstance(),
                        credential
                    ).setApplicationName("Deen App").build()

                    appFolderId = getOrCreateFolder(APP_FOLDER)
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun getOrCreateFolder(folderName: String): String? {
        val service = driveService ?: return null
        val result = service.files().list()
            .setQ("name='$folderName' and mimeType='application/vnd.google-apps.folder' and trashed=false")
            .setSpaces("drive")
            .setFields("files(id)")
            .execute()

        if (result.files.isNotEmpty()) {
            return result.files[0].id
        }

        val folderMetadata = com.google.api.services.drive.model.File().apply {
            name = folderName
            mimeType = "application/vnd.google-apps.folder"
        }
        val folder = service.files().create(folderMetadata)
            .setFields("id")
            .execute()
        return folder.id
    }

    private fun uploadOrUpdateFile(fileName: String, content: String, mimeType: String = "application/json"): Boolean {
        val service = driveService ?: return false
        val folderId = appFolderId ?: return false

        val existingFiles = service.files().list()
            .setQ("name='$fileName' and '$folderId' in parents and trashed=false")
            .setFields("files(id)")
            .execute()

        val mediaContent = ByteArrayContent(mimeType, content.toByteArray())

        if (existingFiles.files.isNotEmpty()) {
            service.files().update(existingFiles.files[0].id, null, mediaContent).execute()
        } else {
            val fileMetadata = com.google.api.services.drive.model.File().apply {
                name = fileName
                parents = listOf(folderId)
            }
            service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute()
        }
        return true
    }

    private fun readFile(fileName: String): String? {
        val service = driveService ?: return null
        val folderId = appFolderId ?: return null

        val result = service.files().list()
            .setQ("name='$fileName' and '$folderId' in parents and trashed=false")
            .setFields("files(id)")
            .execute()

        if (result.files.isEmpty()) return null

        val outputStream = java.io.ByteArrayOutputStream()
        service.files().get(result.files[0].id).executeMediaAndDownloadTo(outputStream)
        return outputStream.toString("UTF-8")
    }

    suspend fun backupProfile(profileJson: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                uploadOrUpdateFile(PROFILE_FILE, profileJson)
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun restoreProfile(): String? {
        return withContext(Dispatchers.IO) {
            try {
                readFile(PROFILE_FILE)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun backupPosts(postsJson: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                uploadOrUpdateFile(POSTS_FILE, postsJson)
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun backupChats(chatsJson: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                uploadOrUpdateFile(CHATS_FILE, chatsJson)
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun backupSettings(settingsJson: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                uploadOrUpdateFile(SETTINGS_FILE, settingsJson)
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun uploadMedia(fileName: String, fileBytes: ByteArray): String? {
        return withContext(Dispatchers.IO) {
            try {
                val service = driveService ?: return@withContext null
                val folderId = appFolderId ?: return@withContext null

                val mimeType = when {
                    fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") -> "image/jpeg"
                    fileName.endsWith(".png") -> "image/png"
                    fileName.endsWith(".mp4") -> "video/mp4"
                    fileName.endsWith(".webm") -> "video/webm"
                    else -> "application/octet-stream"
                }

                val fileMetadata = com.google.api.services.drive.model.File().apply {
                    name = fileName
                    parents = listOf(folderId)
                }
                val mediaContent = ByteArrayContent(mimeType, fileBytes)
                val file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id, webContentLink")
                    .execute()
                file.id
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun syncAll(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                driveService != null && appFolderId != null
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun deleteAllData(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val service = driveService ?: return@withContext false
                val folderId = appFolderId ?: return@withContext false
                service.files().delete(folderId).execute()
                appFolderId = null
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}
