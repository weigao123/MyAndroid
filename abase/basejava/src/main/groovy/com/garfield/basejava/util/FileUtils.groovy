package com.garfield.basejava.util
//@CompileStatic
public class FileUtils {
    static File downloadFile(String fileUrl, String localDir) {
        File file = null

        // Validate url format
        if (fileUrl.startsWith('http://') ||
                fileUrl.startsWith('https://') ||
                fileUrl.startsWith('ftp://')) {
            // Local config file
            String dstFileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1, fileUrl.length())
            dstFileName = localDir + File.separator + dstFileName
            File dstFile = FileUtils.safeCreateFile(dstFileName)

            if (null != dstFile) {
                InputStream input = null
                FileOutputStream output = null
                try {
                    // Remote url
                    URL url = new URL(fileUrl)
                    input = url.openStream()
                    output = new FileOutputStream(dstFile)

                    // Download contents
                    byte[] buffer = new byte[1024 * 8]
                    int count = 0
                    while ((count = input.read(buffer)) > 0) {
                        output.write(buffer, 0, count)
                    }

                    file = dstFile
                } catch (Exception e) {
                    // TODO
                } finally {
                    if (null != output) {
                        output.close()
                        output = null
                    }
                    if (null != input) {
                        input.close()
                        input = null
                    }
                }
            }
        }

        return file
    }

    static void checkIntegrity(File file) {
        if (null == file) {
            return
        }

        File parent = file.getParentFile()
        if (null != parent && !parent.exists()) {
            parent.mkdirs()
        }
    }

    static File safeCreateFile(String filename) {
        if (null == filename || filename.isEmpty()) {
            return null
        }

        File file = new File(filename)
        checkIntegrity(file)
        return file
    }

    static void copyResource(String name, File dest) {
        FileOutputStream os = null
        File parent = dest.getParentFile()
        if (parent != null && (!parent.exists())) {
            parent.mkdirs()
        }
        InputStream is = null
        try {
            is = FileUtils.class.getResourceAsStream("/" + name)
            os = new FileOutputStream(dest, false)

            byte[] buffer = new byte[4096]
            int length
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length)
            }
        } catch (IOException ignore) {
        } finally {
            if (is != null) {
                is.close()
            }
            if (os != null) {
                os.close()
            }
        }
    }

    static void copyResourceIfNoExists(String name, File dest) {
        if (dest != null && dest.exists()) return
        copyResource(name, dest)
    }
}
