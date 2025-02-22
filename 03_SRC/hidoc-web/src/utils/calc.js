export function convertFileSize(size, spaceStrinig = ' ') {
    const units = ['B', 'KB', 'MB', 'GB', 'TB'];
    let unitIndex = 0;

    // 循环将文件大小转换到合适的单位
    while (size >= 1024 && unitIndex < units.length - 1) {
        size = Math.floor(size / 1024);
        unitIndex++;
    }

    return `${size}${spaceStrinig}${units[unitIndex]}`;
}
