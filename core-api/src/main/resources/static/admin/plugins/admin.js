const pluginList = document.getElementById("plugin-list");
const pluginPager = document.getElementById("plugin-pager");
const mediaList = document.getElementById("media-list");
const mediaPager = document.getElementById("media-pager");
const uploadForm = document.getElementById("media-upload");
const uploadInput = document.getElementById("media-files");

let pluginPage = 1;
let mediaPage = 1;

const renderPager = (container, page, totalPages, onPrev, onNext) => {
  container.innerHTML = "";
  const prev = document.createElement("button");
  prev.textContent = "Prev";
  prev.disabled = page <= 1;
  prev.addEventListener("click", onPrev);
  const info = document.createElement("span");
  info.textContent = `Trang ${page} / ${Math.max(totalPages, 1)}`;
  const next = document.createElement("button");
  next.textContent = "Next";
  next.disabled = page >= totalPages;
  next.addEventListener("click", onNext);
  container.append(prev, info, next);
};

const loadPlugins = async () => {
  const response = await fetch(`/api/admin/plugins?page=${pluginPage}&size=9`);
  const data = await response.json();
  pluginList.innerHTML = "";
  data.items.forEach((plugin) => {
    const card = document.createElement("div");
    card.className = "card";
    card.innerHTML = `
      <h3>${plugin.id}</h3>
      <div class="badge">${plugin.state}</div>
      <p>Phiên bản: ${plugin.version}</p>
      <p>Nhà cung cấp: ${plugin.provider || "N/A"}</p>
    `;
    pluginList.appendChild(card);
  });
  renderPager(
    pluginPager,
    data.page,
    data.totalPages,
    () => {
      pluginPage -= 1;
      loadPlugins();
    },
    () => {
      pluginPage += 1;
      loadPlugins();
    }
  );
};

const loadMedia = async () => {
  const response = await fetch(`/api/admin/media?page=${mediaPage}&size=12`);
  const data = await response.json();
  mediaList.innerHTML = "";
  data.items.forEach((item) => {
    const container = document.createElement("div");
    container.className = "media-item";
    const preview = document.createElement("div");
    preview.className = "media-preview";
    if (item.contentType.startsWith("image/")) {
      const img = document.createElement("img");
      img.src = `/api/admin/media/${item.id}`;
      img.alt = item.filename;
      preview.appendChild(img);
    } else {
      preview.textContent = "File";
    }
    const meta = document.createElement("div");
    meta.className = "media-meta";
    meta.innerHTML = `
      <div>${item.filename || "unknown"}</div>
      <div>${item.contentType}</div>
    `;
    const link = document.createElement("a");
    link.href = `/api/admin/media/${item.id}`;
    link.textContent = "Tải xuống";
    container.append(preview, meta, link);
    mediaList.appendChild(container);
  });
  renderPager(
    mediaPager,
    data.page,
    data.totalPages,
    () => {
      mediaPage -= 1;
      loadMedia();
    },
    () => {
      mediaPage += 1;
      loadMedia();
    }
  );
};

uploadForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const files = uploadInput.files;
  if (!files.length) {
    return;
  }
  const formData = new FormData();
  Array.from(files).forEach((file) => formData.append("files", file));
  await fetch("/api/admin/media", {
    method: "POST",
    body: formData,
  });
  uploadInput.value = "";
  mediaPage = 1;
  loadMedia();
});

loadPlugins();
loadMedia();
