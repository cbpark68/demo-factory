package com.demo.factory.common.util;

import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CommonFileUtils {

	public static String uploadFile(String uploadPath, String originalName, byte[] fileData) throws Exception {
		UUID uid = UUID.randomUUID();

		String savedName = uid.toString() + "_" + originalName;

		String savedPath = calcPath(uploadPath);

		File target = new File(uploadPath + savedPath, savedName);

		FileCopyUtils.copy(fileData, target);

		String uploadedFileName = makeUploadedFileName(uploadPath, savedPath, savedName);

		return uploadedFileName;
	}

	private static String makeUploadedFileName(String uploadPath, String path, String fileName) throws Exception {
		String uploadedFileName = uploadPath + path + File.separator + fileName;

		return uploadedFileName.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}

	private static String calcPath(String uploadPath) {
		Calendar cal = Calendar.getInstance();

		String yearPath = File.separator + cal.get(Calendar.YEAR);

		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);

		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

		makeDir(uploadPath, yearPath, monthPath, datePath);

		return datePath;
	}

	private static void makeDir(String uploadPath, String... paths) {
		if (new File(paths[paths.length - 1]).exists()) {
			return;
		}

		for (String path : paths) {
			File dirPath = new File(uploadPath + path);

			if (!dirPath.exists()) {
				dirPath.mkdir();
			}
		}
	}

	public static void makeSiteDir(String uploadPath, String... paths) {
		if (new File(paths[paths.length - 1]).exists()) {
			return;
		}

		for (String path : paths) {
			File dirPath = new File(uploadPath + File.separator + path);

			if (!dirPath.exists()) {
				dirPath.mkdir();
			}
		}
	}

	public static void deleteDir(String uploadPath) throws IOException {
		File dirPath = new File(uploadPath);
		if (dirPath.exists()) {
			FileUtils.cleanDirectory(dirPath);//하위 폴더와 파일 모두 삭제
			if (dirPath.isDirectory()) {
				boolean deleted = dirPath.delete();
			}
		}
	}

	public static MediaType getMediaType(String formatName){
		if(formatName != null) {
			if(formatName.equals("JPG")) {
				return MediaType.IMAGE_JPEG;
			}

			if(formatName.equals("GIF")) {
				return MediaType.IMAGE_GIF;
			}

			if(formatName.equals("PNG")) {
				return MediaType.IMAGE_PNG;
			}
		}

		return null;
	}

	public static void unzipFile(Path sourceZip, Path targetDir) {
		//압축풀기전에 디렉터리 삭제 해야함.
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(sourceZip.toFile()))) {
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				boolean isDirectory = false;
				if (zipEntry.getName().endsWith(File.separator)||
						zipEntry.getName().endsWith("/")) {
					isDirectory = true;
				}
				Path newPath = zipSlipProtect(zipEntry, targetDir);
				if (isDirectory) {
					Files.createDirectories(newPath);
				} else {
					if (newPath.getParent() != null) {
						if (Files.notExists(newPath.getParent())) {
							Files.createDirectories(newPath.getParent());
						}
					}
					Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
				}
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {
		Path targetDirResolved = targetDir.resolve(zipEntry.getName());
		Path normalizePath = targetDirResolved.normalize();
		if (!normalizePath.startsWith(targetDir)) {
			throw new IOException("Bad zip entry: " + zipEntry.getName());
		}
		return normalizePath;
	}

}
