async function histogramFromStdin() {
  process.stdin.setEncoding("utf-8");
  let histogramProcess = new HistogramProcess();
  // 从标准输入中读取文本
  histogramProcess.setContent(await readFromStdin());
  console.log(histogramProcess.calcResult());
}
// 从标准输入中获取数据
async function readFromStdin() {
  let r = "";
  for await (let chunk of process.stdin) {
    r += chunk;
  }
  return r;
}
class HistogramProcess {
  constructor() {
    this.content = "";
  }
  setContent(s) {
    // 删除非字母、数字的字符
    console.log(`s = ${s}`);
    this.content = s.replace(/[^a-zA-Z0-9]/g, "").toUpperCase();
  }
  calcResult() {
    let statistics = {};
    let totalChar = this.content.length;
    // 注意，这里要用 of 而 不是 in
    // for (let v in "abc") console.log(v);  返回的是 0 1 2
    // for (let v of "abc") console.log(v);  返回的是 a b c
    for (let c of this.content) {
      statistics[c] = c in statistics ? statistics[c] + 1 : 1;
    }
    let result = Object.entries(statistics)
      .sort((v1, v2) => (v1[1] === v2[1] ? v2[0] - v1[0] : v2[1] - v1[1])) // 按次数从大到小排序，如果次数相同，按 k 排序
      .map((v) => {
        v[1] = (v[1] * 100) / totalChar; // 将次数转换为百分比
        return v;
      })
      .filter((v) => v[1] > 1) // 过滤掉小于 1% 的字符
      .map((v) => `${v[0]}: ${"#".repeat(Math.round(v[1]))} ${v[1].toFixed(2)}%`) // 转换为柱状图的形式
      .join("\n");
    return result;
  }
}
histogramFromStdin();
