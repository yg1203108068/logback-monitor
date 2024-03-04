import {ApplicationModeEnum} from "../constant/commonEnum";
import {message} from "antd";

/**
 * 应用程序运行模式
 */
export const ApplicationMode = ApplicationModeEnum.DEBUG;


/**
 * 上传属性
 * @type {{headers: {authorization: string}, onChange(*): void, name: string, action: string}}
 */
export const myUploadProps: UploadProps = {
    action: '/api/upload',
    maxCount: 2,
    onChange(info) {
        if (info.file.status !== 'uploading') {
            console.log(info.file, info.fileList);
        }
        if (info.file.status === 'done') {
            message.success(`${info.file.name} 上传成功.`);
        } else if (info.file.status === 'error') {
            if (info.file.response !== undefined) {
                console.log("上传失败", info.file.response)
                message.error(`${info.file.name} ${info.file.response.msg}.`);
                return;
            }
            message.error(`${info.file.name} 上传失败.`);
        }
    },
    onRemove(info) {
        //todo 删除文件
        console.log("文件被删除", info)
    }
};