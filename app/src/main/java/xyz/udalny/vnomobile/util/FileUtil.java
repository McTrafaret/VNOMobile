package xyz.udalny.vnomobile.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class FileUtil {

    private static final String PRIMARY_VOLUME_NAME = "primary";

    public static Bitmap loadBitmapFromFile(View view, File file) {
        try {
            return Glide.with(view)
                    .asBitmap()
                    .load(file)
                    .submit()
                    .get();
        } catch (Exception ex) {
            log.error("While loading bitmap:", ex);
        }
        return null;
    }

    /**
     * This thing is needed because the original client was created only for Windows.
     * Windows treats all files case insensitive, so we have a mess with the directories.
     *
     * @param root -- Root directory
     * @param childPath -- String with relative path to the file
     * @return actual file with such path case-insensitively or null if not found
     */
    public static File getCaseInsensitiveSubFile(File root, String childPath) {
        File result = new File(root, childPath);
        if(result.exists()) {
            return result;
        }

        String[] subfiles = childPath.split("/");

        result = root;
        for(String subfile : subfiles) {
            boolean found = false;
            if(!result.isDirectory() || result.list() == null) {
                return null;
            }
            for(String filename : result.list()) {
                if(subfile.equalsIgnoreCase(filename)) {
                    result = new File(result, filename);
                    found = true;
                    break;
                }
            }
            if(!found) {
                return null;
            }
        }

        return result;
    }

    /**
     * Finds a file with such name case-insensitively and drops extension, so
     * for example if directory contains file "image.png" and we are searching for
     * directory/image it will find this file
     *
     * @param root -- directory to search
     * @param childPath -- relative filename
     * @return file if found or null
     */
    public static File getCaseInsensitiveSubFileDropExtension(File root, String childPath) {

        String[] subfilesNames = childPath.split("/");

        File result = root;
        for(int i = 0; i < subfilesNames.length - 1; i++) {
            String subfile = subfilesNames[i];

            boolean found = false;
            if(!result.isDirectory() || result.list() == null) {
                return null;
            }
            for(String filename : result.list()) {
                if(subfile.equalsIgnoreCase(filename)) {
                    result = new File(result, filename);
                    found = true;
                    break;
                }
            }
            if(!found) {
                return null;
            }
        }

        String filename = subfilesNames[subfilesNames.length-1];

        File[] subfiles = result.listFiles();
        if(subfiles == null) {
            return null;
        }

        for (File subfile : subfiles) {

            if(subfile.isDirectory()) {
                continue;
            }

            String filenameNoExtension = subfile.getName().split("\\.")[0];

            if(filenameNoExtension.equalsIgnoreCase(filename)) {
                return subfile;
            }
        }

        return null;
    }


    @Nullable
    public static String getFullPathFromTreeUri(@Nullable final Uri treeUri, Context con) {
        if (treeUri == null) return null;
        String volumePath = getVolumePath(getVolumeIdFromTreeUri(treeUri),con);
        if (volumePath == null) return File.separator;
        if (volumePath.endsWith(File.separator))
            volumePath = volumePath.substring(0, volumePath.length() - 1);

        String documentPath = getDocumentPathFromTreeUri(treeUri);
        if (documentPath.endsWith(File.separator))
            documentPath = documentPath.substring(0, documentPath.length() - 1);

        if (documentPath.length() > 0) {
            if (documentPath.startsWith(File.separator))
                return volumePath + documentPath;
            else
                return volumePath + File.separator + documentPath;
        }
        else return volumePath;
    }


    private static String getVolumePath(final String volumeId, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return getVolumePathForAndroid11AndAbove(volumeId, context);
        else
            return getVolumePathBeforeAndroid11(volumeId, context);
    }


    private static String getVolumePathBeforeAndroid11(final String volumeId, Context context){
        try {
            StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getUuid = storageVolumeClazz.getMethod("getUuid");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
            Object result = getVolumeList.invoke(mStorageManager);

            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String uuid = (String) getUuid.invoke(storageVolumeElement);
                Boolean primary = (Boolean) isPrimary.invoke(storageVolumeElement);

                if (primary && PRIMARY_VOLUME_NAME.equals(volumeId))    // primary volume?
                    return (String) getPath.invoke(storageVolumeElement);

                if (uuid != null && uuid.equals(volumeId))    // other volumes?
                    return (String) getPath.invoke(storageVolumeElement);
            }
            // not found.
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    private static String getVolumePathForAndroid11AndAbove(final String volumeId, Context context) {
        try {
            StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            List<StorageVolume> storageVolumes = mStorageManager.getStorageVolumes();
            for (StorageVolume storageVolume : storageVolumes) {
                // primary volume?
                if (storageVolume.isPrimary() && PRIMARY_VOLUME_NAME.equals(volumeId))
                    return storageVolume.getDirectory().getPath();

                // other volumes?
                String uuid = storageVolume.getUuid();
                if (uuid != null && uuid.equals(volumeId))
                    return storageVolume.getDirectory().getPath();

            }
            // not found.
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getVolumeIdFromTreeUri(final Uri treeUri) {
        final String docId = DocumentsContract.getTreeDocumentId(treeUri);
        final String[] split = docId.split(":");
        if (split.length > 0) return split[0];
        else return null;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getDocumentPathFromTreeUri(final Uri treeUri) {
        final String docId = DocumentsContract.getTreeDocumentId(treeUri);
        final String[] split = docId.split(":");
        if ((split.length >= 2) && (split[1] != null)) return split[1];
        else return File.separator;
    }
}
