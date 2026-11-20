const omittedFields = ['createDate', 'updateDate', 'creatorId', 'updaterId', 'creator', 'updater', 'extend'];

export function copyWriteFields(entity: Record<string, unknown>): Record<string, unknown> {
    const data = { ...entity };
    for (const field of omittedFields)
        delete data[field];

    return data;
}
