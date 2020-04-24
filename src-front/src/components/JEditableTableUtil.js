
const FormTypes = {
  normal: 'normal',
  input: 'input',
  inputNumber: 'inputNumber',
  checkbox: 'checkbox',
  hidden: 'hidden'
}
const VALIDATE_NO_PASSED = Symbol()
export { FormTypes, VALIDATE_NO_PASSED }

/**
 * 获取指定的 $refs 对象
 * 有时候可能会遇到组件未挂载到页面中的情况，导致无法获取 $refs 中的某个对象
 * 这个方法可以等待挂载完成之后再返回 $refs 的对象，避免报错
 * @author sunjianlei
 **/
export function getRefPromise(vm, name) {
  return new Promise((resolve) => {
    (function next() {
      let ref = vm.$refs[name]
      if (ref) {
        resolve(ref)
      } else {
        setTimeout(() => {
          next()
        }, 10)
      }
    })()
  })
}

/**
 * 一次性验证主表单和所有的次表单
 * @param form 主表单 form 对象
 * @param cases 接收一个数组，每项都是一个JEditableTable实例
 * @returns {Promise<any>}
 * @author sunjianlei
 */
export function validateFormAndTables(form, cases) {

  if (!(form && typeof form.validateFields === 'function')) {
    throw `form 参数需要的是一个form对象，而传入的却是${typeof form}`
  }

  let options = {}
  return new Promise((resolve, reject) => {
    // 验证主表表单
    form.validateFields((err, values) => {
      err ? reject({ error: VALIDATE_NO_PASSED }) : resolve(values)
    })
  }).then(values => {
    Object.assign(options, { formValue: values })
    // 验证所有子表的表单
    return validateTables(cases)
  }).then(all => {
    Object.assign(options, { tablesValue: all })
    return Promise.resolve(options)
  }).catch(error => {
    return Promise.reject(error)
  })

}

/**
 * 验证并获取一个或多个表格的所有值
 * @param cases 接收一个数组，每项都是一个JEditableTable实例
 * @author sunjianlei
 */
export function validateTables(cases) {
  if (!(cases instanceof Array)) {
    throw `'validateTables'函数的'cases'参数需要的是一个数组，而传入的却是${typeof cases}`
  }
  return new Promise((resolve, reject) => {
    let tables = []
    let index = 0;
    (function next() {
      let vm = cases[index]
      vm.getAll(true).then(all => {
        tables[index] = all
        // 判断校验是否全部完成，完成返回成功，否则继续进行下一步校验
        if (++index === cases.length) {
          resolve(tables)
        } else (
          next()
        )
      }, error => {
        // 出现未验证通过的表单，不再进行下一步校验，直接返回失败并跳转到该表格
        if (error === VALIDATE_NO_PASSED) {
          reject({ error: VALIDATE_NO_PASSED, index })
        }
        reject(error)
      })
    })()
  })
}

/**
 * 深度克隆对象、数组
 * @param obj 被克隆的对象
 * @return 克隆后的对象
 */
export function cloneObject(obj) {
  return JSON.parse(JSON.stringify(obj))
}

/**
 * 随机生成字符串
 * @param length 字符串的长度
 * @param chats 可选字符串区间（只会生成传入的字符串中的字符）
 * @return string 生成的字符串
 */
export function randomString(length, chats) {
  if (!length) length = 1
  if (!chats) chats = '0123456789qwertyuioplkjhgfdsazxcvbnm'
  let str = ''
  for (let i = 0; i < length; i++) {
    let num = randomNumber(0, chats.length - 1)
    str += chats[num]
  }
  return str
}

/**
 * 随机生成数字
 *
 * 示例：生成长度为 12 的随机数：randomNumber(12)
 * 示例：生成 3~23 之间的随机数：randomNumber(3, 23)
 *
 * @param1 最小值 | 长度
 * @param2 最大值
 * @return int 生成后的数字
 */
export function randomNumber() {
  // 生成 最小值 到 最大值 区间的随机数
  const random = (min, max) => {
    return Math.floor(Math.random() * (max - min + 1) + min)
  }
  if (arguments.length === 1) {
    let [length] = arguments
    // 生成指定长度的随机数字，首位一定不是 0
    let nums = [...Array(length).keys()].map((i) => (i > 0 ? random(0, 9) : random(1, 9)))
    return parseInt(nums.join(''))
  } else if (arguments.length >= 2) {
    let [min, max] = arguments
    return random(min, max)
  } else {
    return Number.NaN
  }
}
