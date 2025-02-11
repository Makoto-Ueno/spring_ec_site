/**
 * 選択した画像をimageSelectorの画像要素に表示する
 * @param {Event} event イベント
 * @param {string} imageSelector サムネイルを表示するImageタグのセレクタ
 */
function showSelectedImage(event, imageSelector) {
    // ファイルが選択されているか
    if (!event.target || !event.target.files || !event.target.files[0]) {
        console.error(event)
        alert("[ERR01]\n画像の読み込み中にエラーが発生しました。\n繰り返し発生する場合は画面を更新して再度試してください繰り返し発生する場合は画面を更新して再度試してください。")
        return
    }

    // 画像要素が存在するか
    const el = document.querySelector(imageSelector)
    if (!el) {
        console.error(imageSelector, el)
        alert("[ERR02]\nサムネイルの表示でエラーが発生しました。\n繰り返し発生する場合は画面を更新して再度試してください繰り返し発生する場合は画面を更新して再度試してください。")
        return
    }

    // ファイルデータの読み込み
    const reader = new FileReader();
    reader.onload = function (reader) {
        el.src = reader.target.result
    };
    reader.readAsDataURL(event.target.files[0]);

    generateIsImageChangedElement();
}

/**
 * 選択した画像を削除する
 * @param {string} inputSelector
 * @param {string} imageSelector
 */
function deleteSelectedImage(inputSelector, imageSelector) {
    // 要素が存在するか
    const inputEl = document.querySelector(inputSelector)
    const imageEl = document.querySelector(imageSelector)
    if (!inputEl || !imageEl) {
        console.error({ inputSelector, inputEl }, { imageSelector, imageEl })
        alert("[ERR03]\n画像の削除時にエラーが発生しました。\n繰り返し発生する場合は画面を更新して再度試してください繰り返し発生する場合は画面を更新して再度試してください。")
        return
    }
    // ファイルデータの削除
    inputEl.value = ""
    // 表示画像を未選択画像にする
    imageEl.src = "../../common/images/no_image.png"

    generateIsImageChangedElement();
}

function generateIsImageChangedElement() {
    const newElement = document.createElement('input');

    newElement.setAttribute('type', 'hidden');
    newElement.setAttribute('name', 'imageChanged');
    newElement.setAttribute('value', true);

    document.getElementById('edit-form').appendChild(newElement);
}
