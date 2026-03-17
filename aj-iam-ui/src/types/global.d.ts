/**
 * 配置
 */
interface ConfigInterface {
    /**
     * DS
     */
    dsApiRoot: string;
    loginUrl: string;
    thisPageUrl: string;
    iamApi: string;
}

// 扩展 Window 接口
declare global {
  interface Window {
    /**
     * 配置
     */
    config: ConfigInterface;
  }
}

// 这个文件需要导出某些东西，否则会被视为“脚本”而不是“模块”，导致全局扩展不生效。
// export {} 是一个常用的空导出，满足此要求。
export {};