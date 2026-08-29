function initLightbox() {
  if (document.querySelector('.lightbox-overlay')) {
    return;
  }

  let previouslyFocusedElement = null;

  const overlay = document.createElement('div');
  overlay.className = 'lightbox-overlay';
  overlay.setAttribute('role', 'dialog');
  overlay.setAttribute('aria-modal', 'true');
  overlay.setAttribute('aria-label', 'Vista ampliada');
  overlay.setAttribute('aria-hidden', 'true');

  const dialog = document.createElement('div');
  dialog.className = 'lightbox-dialog';

  const image = document.createElement('img');
  image.className = 'lightbox-image';
  image.alt = 'Vista ampliada';

  const closeButton = document.createElement('button');
  closeButton.className = 'lightbox-close';
  closeButton.type = 'button';
  closeButton.setAttribute('aria-label', 'Cerrar');
  closeButton.textContent = '×';

  dialog.appendChild(image);
  dialog.appendChild(closeButton);
  overlay.appendChild(dialog);
  document.body.appendChild(overlay);

  function prepareImages(scope = document) {
    scope.querySelectorAll('.md-content img:not([data-lightbox="disabled"])').forEach(function (contentImage) {
      if (contentImage.dataset.lightboxReady === 'true') {
        return;
      }

      contentImage.dataset.lightboxReady = 'true';
      contentImage.tabIndex = 0;
      contentImage.setAttribute('role', 'button');
      contentImage.setAttribute(
        'aria-label',
        contentImage.alt ? `Ampliar imagen: ${contentImage.alt}` : 'Ampliar imagen'
      );
    });
  }

  function openLightbox(contentImage) {
    previouslyFocusedElement = contentImage;
    image.src = contentImage.currentSrc || contentImage.src;
    image.alt = contentImage.alt || contentImage.title || 'Vista ampliada';
    overlay.classList.add('is-open');
    overlay.setAttribute('aria-hidden', 'false');
    document.body.classList.add('lightbox-open');
    closeButton.focus();
  }

  function closeLightbox() {
    if (!overlay.classList.contains('is-open')) {
      return;
    }

    overlay.classList.remove('is-open');
    overlay.setAttribute('aria-hidden', 'true');
    document.body.classList.remove('lightbox-open');
    image.removeAttribute('src');
    image.alt = 'Vista ampliada';

    if (previouslyFocusedElement && previouslyFocusedElement.isConnected) {
      previouslyFocusedElement.focus();
    }
    previouslyFocusedElement = null;
  }

  document.addEventListener('click', function (event) {
    const clickedImage = event.target.closest('.md-content img');

    if (!clickedImage || clickedImage.dataset.lightbox === 'disabled') {
      return;
    }

    event.preventDefault();
    event.stopPropagation();
    openLightbox(clickedImage);
  });

  document.addEventListener('keydown', function (event) {
    const focusedImage = event.target.closest('.md-content img[data-lightbox-ready="true"]');

    if (focusedImage && (event.key === 'Enter' || event.key === ' ')) {
      event.preventDefault();
      openLightbox(focusedImage);
      return;
    }

    if (!overlay.classList.contains('is-open')) {
      return;
    }

    if (event.key === 'Escape') {
      event.preventDefault();
      closeLightbox();
    } else if (event.key === 'Tab') {
      event.preventDefault();
      closeButton.focus();
    }
  });

  overlay.addEventListener('click', function (event) {
    if (event.target === overlay || event.target === dialog) {
      closeLightbox();
    }
  });

  closeButton.addEventListener('click', closeLightbox);

  prepareImages();

  const observer = new MutationObserver(function () {
    prepareImages();
  });
  observer.observe(document.body, { childList: true, subtree: true });
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initLightbox);
} else {
  initLightbox();
}

window.addEventListener('load', initLightbox);
