// ===== WHEELZY RENTALS - MAIN JS =====

// Mobile nav toggle
function toggleMobileNav() {
    const links = document.querySelector('.nav-links');
    if (links) links.classList.toggle('mobile-open');
}

// Dropdown toggle for navbar user menu
document.addEventListener('DOMContentLoaded', function () {

    // Close dropdowns on outside click
    document.addEventListener('click', function (e) {
        if (!e.target.closest('.nav-dropdown')) {
            document.querySelectorAll('.dropdown-menu').forEach(m => m.style.display = '');
        }
    });

    // Flash message auto-dismiss
    document.querySelectorAll('.alert').forEach(function (alert) {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });

    // Animate stat cards on scroll
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(e => {
            if (e.isIntersecting) {
                e.target.style.animation = 'fadeInUp 0.5s ease forwards';
            }
        });
    }, { threshold: 0.1 });

    document.querySelectorAll('.car-card, .step-card, .why-card, .stat-card').forEach(el => {
        el.style.opacity = '0';
        observer.observe(el);
    });

    // Active nav link
    const path = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(link => {
        if (link.getAttribute('href') === path) {
            link.style.background = 'var(--blue-50)';
            link.style.color = 'var(--blue-700)';
        }
    });

    // Show approval banner if any booking is approved
    document.querySelectorAll('.approval-banner').forEach(b => {
        b.style.display = 'block';
        b.style.background = '#E8F5E9';
        b.style.color = '#1B5E20';
        b.style.padding = '12px 16px';
        b.style.borderRadius = '8px';
        b.style.marginBottom = '1rem';
        b.style.border = '1px solid #A5D6A7';
        b.style.display = 'flex';
        b.style.gap = '8px';
        b.style.alignItems = 'center';
    });
});

// Add CSS keyframe animation
const style = document.createElement('style');
style.textContent = `
@keyframes fadeInUp {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
}
`;
document.head.appendChild(style);
