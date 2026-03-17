/**
 * 树节点
 */
declare type TreeMap = {
    id: number;

    /**
     * 父 id，输入时候必填
     */
    parentId: number;

    /**
     * iView Table 控件的展开属性
     */
    _showChildren: boolean;

    /**
     * 子节点，生成 Tree 之后出现
     */
    children?: TreeMap[];
}